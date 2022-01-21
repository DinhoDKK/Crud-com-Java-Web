<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Produtos</title>

<jsp:include page="../import_bootstrap.jspf" />

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js">
	
</script>
<script>
$(document).ready(function() {
	
	//enviando novos dados ou alterando
	$('#submit').click(function(event){
		var descricao = $('#descricao').val();
		var quantidade = $('#quantidade').val();
		var preco = $('#preco').val();
		var id = $('#idProduto').val();
		$.ajax({
            type:'POST',
            data: {
            	idProduto : id,
            	descricao : descricao,
    			quantidade : quantidade,
    			preco: preco,
    			acao : 'manipular_produto'
            },
            dataType: 'json',
            url:'ProdutoController',
            success: function(produto){
            	/*
            	var $table = $('#tableResposta')
            	
            	var $ancoraApagar =  $("<a/>").addClass('del btn btn-danger btn-sm').attr("href","#").text('Apagar');
            	var $ancoraAlterar =  $("<a/>").addClass('alt btn btn-success btn-sm').attr("href","#").text('Alterar');
            	var novaLinha = $("<tr>").append($("<td>").text(curso.idCurso).addClass('idCell')) 
    			.append($("<td>").text(curso.nome).addClass('nomeCursoCell'))     
    			.append($("<td>").text(curso.quantidadeVagas).addClass('numVagasCell'))
    			.append($("<td>").append($ancoraApagar))
    			.append($("<td>").append($ancoraAlterar));
				
            	if (id == ""){
            		novaLinha.appendTo($table);
            	} else {
            		$('.editMode').replaceWith(novaLinha);
            	}*/
            	$("#idProduto").val("");
                $("#descricao").val("");
                $("#quantidade").val(""); 
                $("#preco").val("");
                
                atualizarPaginacao(); // atualiza o número de páginas
                paginacao('page'); // redefine os itens da página em questão
            },
            error: function(jqXHR, textStatus, message) {
            	alert(jqXHR.responseText);
            }
        });
        
    });
	
	// deletando uma linha da tabela e do banco de dados
	$('#tableResposta').on('click','.del',function(event){
      	var $linhaCorrente = $(this).closest("tr");
        var id = $linhaCorrente.find(".idCell").html();        
        var confirmacao = confirm("Deseja excluir o item: " + id + " ?");
        if (confirmacao == true) {
        	$.ajax({
        	    type:'GET',
        	    data: {
        	    	idProduto : id,
    				acao : 'remover_produto'
        	    },
        	    dataType: 'text',
        	    url:'ProdutoController',
        	    success: function(response){
					//$linhaCorrente.remove();
					
					atualizarPaginacao(); // atualiza o número de páginas
	                paginacao('page'); // redefine os itens da página em questão
        	    },
        	    error: function(jqXHR, textStatus, message) {
        	    	alert(jqXHR.responseText);
        	    }
        	});
        }
    });	

	// enviando uma linha da tabela para editar
	$('#tableResposta').on('click','.alt',function(event){
		
		$('#tableResposta').find('.editMode').each(function(){
			  $(this).removeClass("editMode");
		});
		
      	var $linhaCorrente = $(this).closest("tr");
        var id = $linhaCorrente.find(".idCell").html();  
        var descricao = $linhaCorrente.find(".descricaoCell").html();
        var quantidade = $linhaCorrente.find(".quantidadeCell").html();
        var preco = $linhaCorrente.find(".precoCell").html();
        $("#idProduto").val(id);
        $("#descricao").val(descricao);
        $("#quantidade").val(quantidade);
        $("#preco").val(preco);
        
        $linhaCorrente.addClass('editMode');
    });	
	
	
	// código referente a paginação
	var paginaCorrente = 1;
	var numeroMaximo = ${numPaginas};
	
	function ativarPagina(numeroPagina){
		// remove a marcação de paginação
		$('.pagination').find('.active').each(function(){
			  $(this).removeClass("active");
		});
		$('.pagination').find('.num').each(function(){
			//busca qual item paginação é o atual
			if (numeroPagina == $(this).html()){
				$(this).closest("li").addClass('active');
			}
		});
		
	}
	
	function atualizarPaginacao(){
		$.ajax({
    	    type:'GET',
    	    data: {
    	    	direcao : 'page',
    	    	corrente: paginaCorrente,
				acao : 'numero_pagina'
    	    },
    	    dataType: 'json',
    	    url:'ProdutoController',
    	    success: function(numPaginas){
    	    	if (numeroMaximo < numPaginas){
    	    		numeroMaximo = numPaginas;
    	    		//adiciona página
    	    		var $novali =  $("<li/>").addClass('page-item')
    	    			.append( $("<a>").text(numeroMaximo).addClass('page-link num').attr("href","#")  );   	    					
    	    		$('.pagination li:nth-child(' + numeroMaximo + ')').after($novali);
    	    		
    	    	}else if (numeroMaximo > numPaginas){
    	    		//remove a última página 
    	    		var pag = numeroMaximo + 1
    	    		$('.pagination li:nth-child('+ pag +')').remove();
    	    		numeroMaximo = numPaginas   	    		
    	    	}
    	    	
    	    	  
    	    },
    	    error: function(jqXHR, textStatus, message) {
    	    	alert(jqXHR.responseText);
    	    }
    	});	
	}	
	
	//adiciona o retorno do servidor na tabela
	function povoarTabela(produtos){		
    	var $table = $('#tableResposta')    	
		$.each(produtos, function( key, produto ) {
			var $ancoraApagar =  $("<a/>").addClass('del btn btn-danger btn-sm').attr("href","#").text('Apagar');
	    	var $ancoraAlterar =  $("<a/>").addClass('alt btn btn-success btn-sm').attr("href","#").text('Alterar');
	    	
			var novaLinha = $("<tr>").append($("<td>").text(produto.idProduto).addClass('idCell')) 
				.append($("<td>").text(produto.descricao).addClass('descricaoCell'))     
				.append($("<td>").text(produto.quantidade).addClass('quantidadeCell'))
				.append($("<td>").text(produto.preco).addClass('precoCell'))
				.append($("<td>").append($ancoraApagar))
				.append($("<td>").append($ancoraAlterar));
			novaLinha.appendTo($table);
		});
		
	}
		
	/*
	 * Habilita e desabilita os botões de Antes e Próximo caso necessário
	 */
	function sucessoPaginacao(){
		$("#tableResposta").find("tr:gt(0)").remove(); // limpa a tabela
	    if (paginaCorrente == numeroMaximo){
	    	$("#proximo").closest("li").addClass('disabled');
	    	$('#antes').closest("li").removeClass('disabled');
	    }else if (paginaCorrente == 1){
	    	$("#antes").closest("li").addClass('disabled');	
	    	$('#proximo').closest("li").removeClass('disabled');
	    }else{
	    	$('#antes').closest("li").removeClass('disabled');
	    	$('#proximo').closest("li").removeClass('disabled');
	    }    
	}
	
	function paginacao(novaDirecao){
		$.ajax({
    	    type:'GET',
    	    data: {
    	    	direcao : novaDirecao,
    	    	corrente: paginaCorrente,
				acao : 'paginar_produto'
    	    },
    	    dataType: 'json',
    	    url:'ProdutoController',
    	    success: function(produtos){
    	    	// caso tenha apenas um item na página e foi apagado. Então é feita uma nova requisição 
    	    	// para página anterior.
    	    	if (cursos.length == 0){
    	    		paginaCorrente--;
    	    		paginacao('page');
    	    	}
    	    	
    	    	if (novaDirecao == 'proximo') paginaCorrente++;
    	    	else if (novaDirecao == 'atras') paginaCorrente--;
    	    	
    	    	sucessoPaginacao();	//ativa/desativa botões de Antes e Próximo    		    
    	    	povoarTabela(produtos);//pega o retorno e povoa a tabela
    	    	ativarPagina(paginaCorrente); // ativa a página corrente da paginação
    	    },
    	    error: function(jqXHR, textStatus, message) {
    	    	alert(jqXHR.responseText);
    	    }
    	});	
	}
	
	$('ul').on('click','.num',function(event){
		paginaCorrente = $(this).html(); // pega o número da página
		paginacao('page');		
	});
	
	$("#proximo").click(function(){	
		paginacao('proximo');	
	});
	
	$("#antes").click(function(){	
		paginacao('atras');	
	});
	
		
});


</script>
</head>

<body class="d-flex flex-column h-100">
	
	<jsp:include page="../barra_superior.jsp" />
	
	<main class="flex-shrink-0">
	<div class="container">

		<div class="row">
			<div class="col">
				<form id="formcurso">
				
					<input type="hidden" id="idProduto" /> 
				
					<div class="mb-3">
						<label for="descricao" class="form-label">Descrição</label> <input
							type="text" class="form-control" id="descricao">
					</div>
					<div class="mb-3">
						<label for="quantidade" class="form-label">Quantidade</label> <input
							type="text" class="form-control" id="quantidade">
					</div>
					<div class="mb-3">
						<label for="preco" class="form-label">Preço</label> <input
							type="text" class="form-control" id="preco">
					</div>
					<input type="button" id="submit" class="btn btn-primary" value="Enviar" />
					
				</form>
			</div>
			<div class="col"></div>
			<div class="col"></div>
		</div>

		<div id='row'>

			<div class="col">
				<table class="table" id="tableResposta">
					<thead>
						<tr>
							<th>Id</th>
							<th>Descrição</th>
							<th>Quantidade</th>
							<th>Preço</th>
							<th></th>
							<th></th>
						</tr>
					</thead>
					<c:forEach var="produto" items="${produtos}">
						<tr>
							<td class='idCell'><c:out value="${produto.idProduto}"></c:out></td>
							<td class='descricaoCell'><c:out value="${produto.descricao}"></c:out></td>
							<td class='quantidadeCell'><c:out value="${produto.quantidade}"></c:out></td>
							<td class='precoCell'><c:out value="${produto.preco}"></c:out></td>
							<td><a class='del btn btn-danger btn-sm' href='#'>Apagar</a></td>
							
							<td><a class='alt btn btn-success btn-sm' href='#'>Alterar</a></td>
						</tr>
					</c:forEach>
				</table>
				
				<%-- Definição da barra de navegação --%> 				
				<nav>
				  	<ul class="pagination justify-content-center">
					    <li class="page-item disabled">
					      	<a id="antes" class="page-link" href="#" tabindex="-1">Antes</a>
					    </li>
					    <%-- Constrói as páginas baseado no número de páginas  --%>
						<c:forEach var = "i" begin = "1" end = "${numPaginas}">						
							<c:choose> 
								<%-- Deixa a primeira página ativa --%> 
								       
					         	<c:when test = "${i == 1}">
						            <li class="page-item active">
										<a class="page-link num" href="#"><c:out value="${i}"/></a>
									</li>
					         	</c:when>         
					         	<c:otherwise>
						            <li class="page-item">
										<a class="page-link num" href="#"><c:out value="${i}"/></a>
									</li>
					         	</c:otherwise>
					       	</c:choose>							
			           	</c:forEach>
						<li class="page-item">
					      	<a id="proximo" class="page-link" href="#">Próximo</a>
					    </li>
				  	</ul>
				</nav>
				
								
			</div>
		</div>
	</div>
	</main>
</body>
</html>