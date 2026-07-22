[CmdletBinding()]
param(
    [string]$ProjectRoot,
    [ValidatePattern('^[A-Za-z0-9_-]+$')]
    [string]$Environment = 'dev',
    [switch]$RequireSecrets
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$failures = [System.Collections.Generic.List[string]]::new()
$warnings = [System.Collections.Generic.List[string]]::new()
$passes = [System.Collections.Generic.List[string]]::new()

function Write-Result {
    param(
        [ValidateSet('PASS', 'WARN', 'FAIL', 'INFO')]
        [string]$Level,
        [string]$Message
    )

    switch ($Level) {
        'PASS' {
            $passes.Add($Message)
            Write-Host "[PASS] $Message" -ForegroundColor Green
        }
        'WARN' {
            $warnings.Add($Message)
            if ($env:GITHUB_ACTIONS -eq 'true') {
                Write-Host "::warning::$Message"
            }
            else {
                Write-Host "[WARN] $Message" -ForegroundColor Yellow
            }
        }
        'FAIL' {
            $failures.Add($Message)
            if ($env:GITHUB_ACTIONS -eq 'true') {
                Write-Host "::error::$Message"
            }
            else {
                Write-Host "[FAIL] $Message" -ForegroundColor Red
            }
        }
        'INFO' {
            Write-Host "[INFO] $Message" -ForegroundColor Cyan
        }
    }
}

function Read-Properties {
    param([string]$Path)

    $result = @{}

    foreach ($rawLine in Get-Content -LiteralPath $Path -Encoding UTF8) {
        $line = $rawLine.Trim()

        if (
            [string]::IsNullOrWhiteSpace($line) -or
            $line.StartsWith('#') -or
            $line.StartsWith('!')
        ) {
            continue
        }

        $index = $line.IndexOf('=')

        if ($index -lt 1) {
            Write-Result FAIL "Linha inválida em '$Path': $rawLine"
            continue
        }

        $key = $line.Substring(0, $index).Trim()
        $value = $line.Substring($index + 1).Trim()

        if ($result.ContainsKey($key)) {
            Write-Result FAIL "Propriedade duplicada '$key' em '$Path'."
            continue
        }

        $result[$key] = $value
    }

    return $result
}

function Test-RequiredFile {
    param([string]$RelativePath)

    $fullPath = Join-Path $script:Root $RelativePath

    if (Test-Path -LiteralPath $fullPath -PathType Leaf) {
        Write-Result PASS "Arquivo encontrado: $RelativePath"
        return $true
    }

    Write-Result FAIL "Arquivo obrigatório ausente: $RelativePath"
    return $false
}

function Test-RequiredVariable {
    param(
        [string]$Name,
        [int]$MinimumLength = 1
    )

    $value = [Environment]::GetEnvironmentVariable($Name)

    if ([string]::IsNullOrWhiteSpace($value)) {
        Write-Result FAIL "Variável obrigatória não definida: $Name"
        return
    }

    if ($value.Length -lt $MinimumLength) {
        Write-Result FAIL "$Name deve possuir pelo menos $MinimumLength caracteres."
        return
    }

    Write-Result PASS "Variável obrigatória configurada: $Name"
}

if ([string]::IsNullOrWhiteSpace($ProjectRoot)) {
    $candidate = Join-Path $PSScriptRoot '..\..'

    if (Test-Path -LiteralPath $candidate -PathType Container) {
        $ProjectRoot = $candidate
    }
    else {
        $ProjectRoot = (Get-Location).Path
    }
}

if (-not (Test-Path -LiteralPath $ProjectRoot -PathType Container)) {
    throw "Diretório do projeto não encontrado: $ProjectRoot"
}

$script:Root = (Resolve-Path -LiteralPath $ProjectRoot).Path

Write-Host ''
Write-Host '==============================================' -ForegroundColor Cyan
Write-Host '        CONFIGURATION QUALITY GATE' -ForegroundColor Cyan
Write-Host '==============================================' -ForegroundColor Cyan
Write-Result INFO "Projeto: $script:Root"
Write-Result INFO "Ambiente: $Environment"
Write-Host ''

$pomExists = Test-RequiredFile 'pom.xml'
$environmentManagerExists = Test-RequiredFile (
    'src/main/java/br/com/fernandouchoa/qa/core/config/EnvironmentManager.java'
)
$junitExists = Test-RequiredFile 'src/test/resources/junit-platform.properties'
$usersExists = Test-RequiredFile 'src/test/resources/testdata/users.json'

$environmentRelativePath = (
    "src/test/resources/environments/{0}.properties" -f $Environment
)
$environmentExists = Test-RequiredFile $environmentRelativePath

if ($pomExists) {
    $pomPath = Join-Path $script:Root 'pom.xml'

    try {
        [xml]$pom = Get-Content -LiteralPath $pomPath -Raw -Encoding UTF8
        Write-Result PASS 'pom.xml possui XML válido.'

        $javaVersion = [string]$pom.project.properties.'java.version'
        if ($javaVersion -eq '17') {
            Write-Result PASS 'Java 17 confirmado no pom.xml.'
        }
        else {
            Write-Result FAIL "pom.xml deve declarar java.version=17. Atual: '$javaVersion'."
        }

        $artifactId = [string]$pom.project.artifactId
        if ($artifactId -eq 'qa-enterprise-automation-framework') {
            Write-Result PASS 'Artifact Maven esperado confirmado.'
        }
        else {
            Write-Result FAIL "Artifact Maven inesperado: '$artifactId'."
        }
    }
    catch {
        Write-Result FAIL "pom.xml inválido: $($_.Exception.Message)"
    }
}

if ($environmentExists) {
    $environmentPath = Join-Path $script:Root $environmentRelativePath
    $properties = Read-Properties $environmentPath

    foreach ($key in @('base.url', 'browser', 'headless', 'timeout')) {
        if (
            -not $properties.ContainsKey($key) -or
            [string]::IsNullOrWhiteSpace($properties[$key])
        ) {
            Write-Result FAIL "Propriedade obrigatória ausente ou vazia: $key"
        }
        else {
            Write-Result PASS "Propriedade configurada: $key"
        }
    }

    if ($properties.ContainsKey('base.url')) {
        $uri = $null
        $validUri = [Uri]::TryCreate(
            $properties['base.url'],
            [UriKind]::Absolute,
            [ref]$uri
        )

        if (
            -not $validUri -or
            $uri.Scheme -notin @('http', 'https') -or
            -not [string]::IsNullOrEmpty($uri.UserInfo)
        ) {
            Write-Result FAIL (
                'base.url deve ser uma URL HTTP/HTTPS absoluta e sem credenciais.'
            )
        }
        else {
            Write-Result PASS 'base.url possui formato seguro e válido.'
        }
    }

    if ($properties.ContainsKey('browser')) {
        $browser = $properties['browser'].ToLowerInvariant()

        if ($browser -in @('chromium', 'firefox', 'webkit')) {
            Write-Result PASS "Browser suportado: $browser"
        }
        else {
            Write-Result FAIL (
                "Browser inválido: '$browser'. Use chromium, firefox ou webkit."
            )
        }
    }

    if ($properties.ContainsKey('headless')) {
        $headless = $false

        if ([bool]::TryParse($properties['headless'], [ref]$headless)) {
            Write-Result PASS "Configuração headless válida: $headless"
        }
        else {
            Write-Result FAIL 'headless deve ser true ou false.'
        }
    }

    if ($properties.ContainsKey('timeout')) {
        $timeout = 0

        if (
            [int]::TryParse($properties['timeout'], [ref]$timeout) -and
            $timeout -ge 1000 -and
            $timeout -le 300000
        ) {
            Write-Result PASS "Timeout válido: $timeout ms"
        }
        else {
            Write-Result FAIL (
                'timeout deve ser um inteiro entre 1000 e 300000 milissegundos.'
            )
        }
    }
}

if ($junitExists) {
    $junitPath = Join-Path $script:Root (
        'src/test/resources/junit-platform.properties'
    )
    $junit = Read-Properties $junitPath

    if ($junit['junit.jupiter.execution.parallel.enabled'] -eq 'true') {
        Write-Result PASS 'Execução paralela do JUnit está habilitada.'
    }
    else {
        Write-Result FAIL (
            'junit.jupiter.execution.parallel.enabled deve ser true.'
        )
    }

    if ($junit['junit.jupiter.execution.parallel.config.strategy'] -eq 'fixed') {
        Write-Result PASS 'Estratégia paralela fixed confirmada.'
    }
    else {
        Write-Result FAIL 'A estratégia paralela do JUnit deve ser fixed.'
    }

    $parallelism = 0
    $parallelismValue = $junit[
        'junit.jupiter.execution.parallel.config.fixed.parallelism'
    ]

    if (
        [int]::TryParse($parallelismValue, [ref]$parallelism) -and
        $parallelism -ge 1 -and
        $parallelism -le 16
    ) {
        Write-Result PASS "Quantidade de workers válida: $parallelism"
    }
    else {
        Write-Result FAIL 'A quantidade de workers deve estar entre 1 e 16.'
    }
}

if ($usersExists) {
    $usersPath = Join-Path $script:Root (
        'src/test/resources/testdata/users.json'
    )

    try {
        $users = Get-Content -LiteralPath $usersPath -Raw -Encoding UTF8 |
            ConvertFrom-Json

        Write-Result PASS 'users.json possui JSON válido.'

        if ($users.validUser.email -eq '${TEST_USER_EMAIL}') {
            Write-Result PASS 'E-mail de teste está externalizado.'
        }
        else {
            Write-Result FAIL (
                'users.json deve usar ${TEST_USER_EMAIL}; não versione e-mail real.'
            )
        }

        if ($users.validUser.password -eq '${TEST_USER_PASSWORD}') {
            Write-Result PASS 'Senha de teste está externalizada.'
        }
        else {
            Write-Result FAIL (
                'users.json deve usar ${TEST_USER_PASSWORD}; não versione senha real.'
            )
        }
    }
    catch {
        Write-Result FAIL "users.json inválido: $($_.Exception.Message)"
    }
}

if ($RequireSecrets) {
    Test-RequiredVariable 'TEST_USER_EMAIL' 5
    Test-RequiredVariable 'TEST_USER_PASSWORD' 8
    Test-RequiredVariable 'NVD_API_KEY' 1
}
else {
    Write-Result INFO (
        'Secrets não foram exigidos. Use -RequireSecrets na pipeline.'
    )
}

$gitCommand = Get-Command git -ErrorAction SilentlyContinue

if ($null -eq $gitCommand) {
    Write-Result WARN (
        'Git não encontrado; arquivos sensíveis rastreados não foram verificados.'
    )
}
else {
    Push-Location $script:Root

    try {
        $tracked = @(& git ls-files)

        if ($LASTEXITCODE -ne 0) {
            Write-Result WARN 'Não foi possível listar os arquivos rastreados.'
        }
        else {
            $forbidden = @(
                $tracked | Where-Object {
                    $name = [IO.Path]::GetFileName($_)
                    $allowedExample = $name -match '^\.env\.(example|sample|template)$'

                    -not $allowedExample -and (
                        $name -eq '.env' -or
                        $name -match '^\.env\.' -or
                        $name -match '\.(p12|pfx|pem|key|jks|keystore)$'
                    )
                }
            )

            if ($forbidden.Count -gt 0) {
                foreach ($file in $forbidden) {
                    Write-Result FAIL "Arquivo sensível versionado: $file"
                }
            }
            else {
                Write-Result PASS (
                    'Nenhum arquivo sensível proibido está rastreado pelo Git.'
                )
            }
        }
    }
    finally {
        Pop-Location
    }
}

Write-Host ''
Write-Host '==============================================' -ForegroundColor Cyan
Write-Host '       CONFIGURATION GATE SUMMARY' -ForegroundColor Cyan
Write-Host '==============================================' -ForegroundColor Cyan
Write-Host "Passed:   $($passes.Count)"
Write-Host "Warnings: $($warnings.Count)"
Write-Host "Failures: $($failures.Count)"
Write-Host '==============================================' -ForegroundColor Cyan

if ($failures.Count -gt 0) {
    Write-Host 'CONFIGURATION GATE: FAILED' -ForegroundColor Red
    exit 1
}

Write-Host 'CONFIGURATION GATE: PASSED' -ForegroundColor Green
exit 0
