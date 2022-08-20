# Check if module exists
Install-Module powershell-yaml
Import-Module powershell-yaml

$origDir = Get-Location
$productConfig = Get-Content -Path ./product.json | ConvertFrom-Json
$resDir = $productConfig.resourcesDir

Set-Location "../$resDir"

$pluginConfig = [PSCustomObject]@{
    name = $productConfig.pluginName
    version = $productConfig.version
    main = $productConfig.mainClass
    "api-version" = $productConfig.mcVersion
    prefix = $productConfig.logPrefix
}

$pluginConfig | ConvertTo-Yaml | Out-File "plugin.yml"
Set-Location $origDir