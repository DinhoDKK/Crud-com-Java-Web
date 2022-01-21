<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Projeto Usuarios</title>



<jsp:include page="import_bootstrap.jspf" />

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js">
	
</script>
<script>
$(document).ready(function() {
	
	//enviando novos dados	
	$('#submit').click( function(event){
		
		var email_form = $('#email_form').val();
		var senha_form = $('#senha_form').val();
		
		$.ajax({
            type:'POST',
            data: {
            	email : email_form,
            	senha : senha_form,
    			acao : 'login'
            },
            dataType: 'json',
            url:'UsuarioController',
            success: function(responseText){
            	$("#email_form").val(""); 
                $("#senha_form").val("");
                if (responseText == 'OK'){
                	window.location.href = 'ProdutoController?acao=gerenciar';
                }else{
                	$("#erroLogin").show();
                }
            },
            error: function(jqXHR, textStatus, message) {
            	$("#erro").show();
            }
        });
        
    });
});	
</script>
</head>

<body class="d-flex flex-column h-100">
	
	<jsp:include page="barra_superior.jsp" />
	
	<main class="flex-shrink-0">
	
 
<div class="container">
		<div class="row">
			<div class="col"></div>
			<div class="col">
				<form>
				  <div class="mb-3">
				    <label for="email" class="form-label">Email</label>
				    <input type="email" class="form-control" id="email_form">
				  </div>
				  <div class="mb-3">
				    <label for="senha" class="form-label">Senha</label>
				    <input type="password" class="form-control" id="senha_form">
				  </div>
				  <input type="button" id="submit" class="btn btn-primary" value="Entrar" />
				  <a class='btn btn-secondary' href='UsuarioController?acao=nova_conta'>Criar Conta</a>
				</form>	
				<div id="erro" style="display:none"  class="alert alert-danger">
				  	 Problemas ao tentar logar.
				</div>
				<div id="erroLogin" style="display:none"  class="alert alert-danger">
					 Erro de login ou senha. 
				</div>			
			</div>
			<div class="col"></div>
		</div>
	</div>

	</main>
</body>
</html>