@echo off
REM ==============================================
REM Simple Brainf*ck Interpreter for Windows
REM Usage: runbf.bat program.bf
REM ==============================================

if "%~1"=="" (
    echo Usage: %~nx0 program.bf
    exit /b 1
)

powershell -NoProfile -Command ^
    "$code = Get-Content '%~1' -Raw | ForEach-Object { ($_ -split '') -match '[\+\-\<\>\[\]\.,]' } | Out-String; " ^
    "$cells = @(0)*30000; $ptr = 0; $stack = @(); $jump = @{}; " ^
    "for ($i=0; $i -lt $code.Length; $i++) { if($code[$i] -eq '['){ $stack += $i } elseif($code[$i] -eq ']'){ $s = $stack[-1]; $stack = $stack[0..($stack.Length-2)]; $jump[$s]=$i; $jump[$i]=$s } } " ^
    "$i = 0; while ($i -lt $code.Length) { switch ($code[$i]) { " ^
    "'>' { $ptr++ } '<' { $ptr-- } '+' { $cells[$ptr] = ($cells[$ptr] + 1) -band 255 } '-' { $cells[$ptr] = ($cells[$ptr] - 1) -band 255 } '.' { [Console]::Write([char]$cells[$ptr]) } ',' { $cells[$ptr] = [byte][Console]::Read() } '[' { if ($cells[$ptr] -eq 0) { $i = $jump[$i] } } ']' { if ($cells[$ptr] -ne 0) { $i = $jump[$i] } } }; $i++ }"

@REM command: .\BF.bat <file_path>.bf