<%@page import="br.com.projetousuarios.model.Usuario"%>
<header>
	<!-- Fixed navbar -->
	<nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
		<div class="container-fluid">

			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarCollapse"
				aria-controls="navbarCollapse" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarCollapse">
				<ul class="navbar-nav me-auto mb-2 mb-md-0">
					<li class="nav-item"><a class="nav-link active"
						aria-current="page" href="#">Home</a></li>
				</ul>
				<% Usuario usuario = (Usuario) session.getAttribute("usuario"); %>
				<% if (usuario!= null){ %>				
					<ul class="navbar-nav flex-row ml-md-auto d-none d-md-flex">
						<li class="nav-item"><a class="nav-link"
							href="CursoController?acao=gerenciar">Produtos</a>
						</li>
						<li class="nav-item"><a class="nav-link active"
							href="#"><%=usuario.getNome()%></a>
						</li>
						<li class="nav-item"><a class="nav-link active"
							href="UsuarioController?acao=logout">Sair</a>
						</li>
	
					</ul>
				<% } %>
				<!--  
				<form class="d-flex">
					<input class="form-control me-2" type="search" placeholder="Buscar"
						aria-label="Search">
					<button class="btn btn-outline-success" type="submit">Buscar</button>
				</form>
				-->
			</div>
		</div>
	</nav>
</header>