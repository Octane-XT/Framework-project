<%@ page import="java.util.*,model.Emp" %>
<% List<Emp> le = (List<Emp>) request.getAttribute("listemp"); %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    List employe
    <ul>
        <% for(int i=0; i<le.size(); i++){ %>
        <li><% out.println(le.get(i).getNom()); %><a href='Emp-id.go?id=<%= i %>&date="2024-06-12"'> More info.</a></li>
        <% } %>
    </ul>
    
</body>
</html>