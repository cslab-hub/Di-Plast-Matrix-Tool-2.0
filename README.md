# Di-Plast Matrix Tool2.0

The Matrix-Tool is a web application based on PrimeFaces (JSF) and part of the Di-Plast toolkit set. The tools goal is to increase recyclate uptake by improving the finding process between recyclate producer and converters. It works in two ways: It can help to replace virgin plastics material with recycled materials by providing information on available recyclates or by making suggestens for alternatives based on expert knowledge and machine learning. On the other hand the tool can give an overview on highly demanded properties, to indicate new market niches. A few more advantages can be taken from the following figure:

![benefits figure](https://github.com/cslab-hub/Di-Plast-Matrix-Tool-2.0/blob/main/Files/Tool-benefits-9.JPG)

To note is, that the tool is not a market place and thus not supports prices, amounts or buy options.

For more information as well as on a guideline how to use the tool we refer to the Di-Plast Knowledge Hub via: https://di-plast.sis.cs.uos.de/Wiki.jsp?page=Main

## Getting Started

The contained Maven NetBeans Web project can be imported and build. A WebServer supporting JSF is needed to run the web application, like for example a Tomcat. The standard login is ID: Admin PW: admin

### Database

For data storage purposes we use hibernate. To enable an external database please set it up in the /main/src/main/resources/hibernate.cfg.xml 

### Mail Service

If you want to use the Mail-Service please make sure to set up a mail server and change MailService.java accordingly. Further change all used mails to your needs. 

### Disclaimer, data policy and legal notice

Please note that when providing the application those three files need to be prepared. Initially they are empty.

## Default Access
Default amin account:<br>
Id: Admin <br>
Pw: admin<br>

## Funding Information

The Matrix tool is funded by the Interreg North-West Europe program (Interreg NWE), project Di-Plast - Digital Circular Economy for the Plastics Industry (NWE729, https://www.nweurope.eu/projects/project-search/di-plast-digital-circular-economy-for-the-plastics-industry/). For more information visit the Di-Plast Knowledge Hub via: https://di-plast.sis.cs.uos.de/Wiki.jsp?page=Main
