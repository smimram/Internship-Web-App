# Internship-Web-App
Creation of a website for internship management of Ecole polytechnique. This project is done under the supervision of INF553's teachers.

### Students:
- Yujia Fu
- Côme de Germay
- Rémi Delacourt 
- Aleksa Marusic
- Victor Radermecker

### Supervisor:
- Ioana Manolescu


## Installation and project settings


We encourage to use Eclipse JEE environment to clone this project. It can be downloaded under the following link: https://www.eclipse.org/downloads/packages/release/kepler/sr2/eclipse-ide-java-ee-developers

Clone the project using:

`git clone https://github.com/victor-radermecker/Internship-Web-App.git`

The project requires the following libraries:

- postgresql-42.1.4.jar
- javax.mail.jar
- commons-lang3-3.6.jar
- commons-text-1.1.jar
- javax.servlet.jsp.jstl-1.2.1.jar
- javax.servlet.jsp.jstl-api1.2.1.jar

All those libraries are included in the GitHub repository so installation should be straight forward.

<span style="color:red"> **Please create a local server before launching the project.** </span>


### Detailed instructions

1. Clone this project.

2. Set the working directory of Eclipse to the repository `servlets-workspace`.

3. Set the java development kit to jdk-14 which can be downloaded here: https://www.oracle.com/fr/java/technologies/javase/jdk14-archive-downloads.html

4. Create the server using Apache Tomcat v9.0 on localhost.

5. Create the postgreSQL database using the tableCreations.SQL file. 

6. Load the project in Eclipse if it's not loaded automatically.

7. Change the dbUtils.java file to make sure that the server connexion credentials are valid.

8. Start the server and enjoy :)




## 


The web application in structured in the following hierarchy:
- Java Servlets under src/edu.polytechnique.inf553 : Each servlet communicates with a JSP page to request and send parameters. Servlets are coded using java and SQL. The database is managed by postgreSQL.
- JSP files under WebContent : Each JSP file contains the HTML code for a particular page. Sometimes, some javascript code is embeded inside for dynamic programming purposes.
- CSS files for design purposes hosted under WebContent/css directory






