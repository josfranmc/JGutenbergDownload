# JGutenbergDownload
It allows to download books from the Gutenberg project repositories.

## Content
+ javadoc: code documentation
+ src: source code

## How to use
From command line:

java -jar JGutenbergDownload-2.0-shaded.jar [options]

options:

-f type of files to download (default txt)  
-l language of books to download (default en)
-s download path (default program's folder)  
-o overwrite existing files (default false) 
-d delay between downloads in milliseconds (default 2000)  
-z unzip downloads (default true) 
-m max number of downloads (default 10, 0 for dowload all)    
-h show options list

## Notas  
A log file in a folder called log is generated.
