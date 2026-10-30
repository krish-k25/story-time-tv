$ErrorActionPreference = 'Stop'
$sdk = if ($env:ANDROID_HOME) { $env:ANDROID_HOME } else { $env:ANDROID_SDK_ROOT }
if (-not $sdk) { throw 'Set ANDROID_HOME or ANDROID_SDK_ROOT to your Android SDK directory.' }
$tools = Join-Path $sdk 'build-tools/35.0.0'
$android = Join-Path $sdk 'platforms/android-35/android.jar'
foreach ($file in @($android, "$tools/aapt.exe", "$tools/d8.bat", "$tools/zipalign.exe", "$tools/apksigner.bat")) {
    if (-not (Test-Path $file)) { throw "Missing Android SDK component: $file" }
}
Set-Location $PSScriptRoot
New-Item -ItemType Directory -Force out, out/classes, out/dex | Out-Null
function Run-Checked { param([scriptblock]$Command) & $Command; if ($LASTEXITCODE -ne 0) { throw "Command failed: $LASTEXITCODE" } }
Run-Checked { & "$tools/aapt.exe" package -f -m -J out -M AndroidManifest.xml -S res -I $android }
$sources = @(Get-ChildItem src -Recurse -Filter *.java | ForEach-Object FullName) + @(Get-ChildItem out/com -Recurse -Filter *.java | ForEach-Object FullName)
Run-Checked { & javac -encoding UTF-8 -source 8 -target 8 -classpath $android -d out/classes $sources }
$classes = @(Get-ChildItem out/classes -Recurse -Filter *.class | ForEach-Object FullName)
Run-Checked { & "$tools/d8.bat" --min-api 24 --lib $android --output out/dex $classes }
Run-Checked { & "$tools/aapt.exe" package -f -M AndroidManifest.xml -S res -A assets -I $android -F out/unsigned.apk }
Add-Type -AssemblyName System.IO.Compression.FileSystem
$zip = [System.IO.Compression.ZipFile]::Open((Join-Path $PSScriptRoot 'out/unsigned.apk'), [System.IO.Compression.ZipArchiveMode]::Update)
try { [System.IO.Compression.ZipFileExtensions]::CreateEntryFromFile($zip, (Join-Path $PSScriptRoot 'out/dex/classes.dex'), 'classes.dex') | Out-Null } finally { $zip.Dispose() }
Run-Checked { & "$tools/zipalign.exe" -f 4 out/unsigned.apk out/aligned-debug.apk }
if (-not (Test-Path out/debug.keystore)) {
    Run-Checked { & keytool -genkeypair -keystore out/debug.keystore -storepass android -keypass android -alias androiddebugkey -dname 'CN=Android Debug,O=Android,C=US' -keyalg RSA -keysize 2048 -validity 10000 }
}
Run-Checked { & "$tools/apksigner.bat" sign --ks out/debug.keystore --ks-key-alias androiddebugkey --ks-pass pass:android --key-pass pass:android --out out/story-time-debug.apk out/aligned-debug.apk }
Run-Checked { & "$tools/apksigner.bat" verify out/story-time-debug.apk }
Write-Host 'Built out/story-time-debug.apk'
