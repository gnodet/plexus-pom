<%@ taglib uri="/webwork" prefix="ww" %>

<html>
  <head>
    <title>Plexus Example Webapp resource</title>
  </head>

  <body>
    <p>
      Resource Modification Page

    </p>
    <ww:actionerror/>
    <ww:form action="saveResource" method="post">
      <ww:hidden name="save" value="true"/>

     <ww:textfield label="identifer" name="identifer"/> <br/>
     <ww:checkbox label="isPattern" name="pattern"/> <br/>


      <center><ww:submit/></center>
    </ww:form>

  </body>
</html>