# JGutenbergDownload
This software allows you to download books from the Gutenberg project repositories.  

The Gutenberg project website is intended for human users only. If you want to download many books using an automated tool like this, keep in mind that your ip address can be blocked temporarily or permanently.  
The Gutenberg project is a solidarity project based on the hard work of thousands of volunteers. So please, don't abuse.

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

## Getting Started

The project is a Maven project, so you can import it in your favorite IDE as any other Maven project.

~~~
mvn install
~~~

will install the artifact in your local repository, being ready to be used as a dependency in any project:

~~~
<dependency>
  <groupId>org.josfranmc.gutenberg</groupId>
  <artifactId>JGutenbergDownload</artifactId>
  <version>2.0.1</version>
</dependency>
~~~

When you build the project with Maven you get two jars in the target directory: _JGutenbergDownload-2.0.1.jar_ and _JGutenbergDownload-2.0.1-shaded.jar_. The first one is the standard jar of the project. The second one is an _uber_ jar with all necessary dependencies, which is suitable to use from command line.    

Download the latest _uber_ jar from [Releases](https://github.com/josfranmc/JGutenbergDownload/releases).

The project was developed with openjdk 11. Jar files should run without problems with a jre 1.8 or higher.

## Usage

The main class to use is `JGutenbergDownload`, which offers some methods to set up the download process.

The following code will download 20 english books in a folder called _mybooks_:  

~~~
try {
    JGutenbergDownload jg = new JGutenbergDownload();
    jg.setLanguage("en");
    jg.setSavePath("mybooks");
    jg.setMaxFilesToDownload(20);
    jg.downloadBooks();
} catch(GutenbergException e) {
    System.err.println(e.getMessage());
}
~~~

You can also use the `DownloadParams` class to set up:  

~~~
try {
    DownloadParams params = new DownloadParams();
    params.setLanguage("en");
    params.setSavePath("mybooks");
    params.setMaxFilesToDownload(20);
    
    JGutenbergDownload jg = new JGutenbergDownload();
    jg.setParameters(params);
    jg.downloadBooks();
} catch(GutenbergException e) {
    System.err.println(e.getMessage());
}
~~~

Finally, you can execute the `JGutenbergDownload`'s main method by passing the setting options as argument. The following code perfoms the same function as the previous ones:  

~~~
try {
    String[] args = {"-l", "en", "-s", "mybooks", "-m", "20" };
    JGutenbergDownload.main(args);
} catch(GutenbergException e) {
    System.err.println(e.getMessage());
}
~~~

These are the options you can use as arguments:  

~~~
-f xxx (xxx type of files to download, default: txt)
-l xx  (xx  language of books to download, default: es)
-s xxx (xxx download path on local machine, default: program folder)
-d xxx (xxx delay between downloads in milliseconds, default 2000)
-m xx  (xx  max number of downloads (default 10, 0 for download all)
-o     (    overwrite existing files, default: false)
-z     (    don't unzip downloads, default: true)
(only -h to show options list);
~~~

---

It is possible to run the program from the command line. To this purpose, you may use the _JGutenbergDownload-2.0.1-shaded.jar_ package with any of the options above:

~~~
java -jar JGutenbergDownload-2.1-shaded.jar -l en -s mybooks -m 20
~~~

## License

[GPLv3](https://www.gnu.org/licenses/gpl-3.0) or later, see
[LICENSE](LICENSE) for more details.
