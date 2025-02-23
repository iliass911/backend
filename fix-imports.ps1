# Get all Java files recursively
Get-ChildItem -Path . -Filter *.java -Recurse | ForEach-Object {
    $content = Get-Content $_.FullName -Raw
    
    # Replace jakarta imports with javax
    $content = $content -replace "import jakarta\.persistence\.", "import javax.persistence."
    $content = $content -replace "import jakarta\.validation\.", "import javax.validation."
    $content = $content -replace "import jakarta\.servlet\.", "import javax.servlet."
    $content = $content -replace "import jakarta\.transaction\.", "import javax.transaction."
    $content = $content -replace "import jakarta\.annotation\.", "import javax.annotation."
    
    # Write the modified content back to the file
    Set-Content $_.FullName $content

    # Output which file was processed
    Write-Host "Processed: " $_.FullName
}

Write-Host "`nImport replacement complete!`n"
