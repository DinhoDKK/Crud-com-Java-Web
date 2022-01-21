package br.com.projetousuarios.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;

import br.com.projetousuarios.dao.ProdutoDao;
import br.com.projetousuarios.model.Produto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ProdutoController")
public class ProdutoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int PRODUTO_POR_PAGINA = 3;
	
	public ProdutoController() {
		super();
	}

	private void processarRequisicao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String action = request.getParameter("acao");

		if (action == null) {
			throw new ServletException("Sem ação especificada.");
		} else if (action.equals("manipular_produto")) {
			String idProduto = request.getParameter("idProduto");
			if ((idProduto == null ) || (idProduto.equals(""))){
				adicionarProduto(request, response);
			}else {
				alterarProduto(request, response);
			}			 
		} else if (action.equals("gerenciar")) {
			listarProdutos(request, response);
		} else if (action.equals("remover_produto")) {
			apagarProduto(request, response);
		} else if (action.equals("paginar_produto")) {
			paginarProduto(request, response);
		} else if (action.equals("numero_pagina")) {
			numeroPagina(request, response);
		}
	}

	private void numeroPagina(HttpServletRequest request, HttpServletResponse response) throws IOException{
		ProdutoDao produtoDao;
		
		try {
			produtoDao = new ProdutoDao();
			int quantProdutos = produtoDao.quantidadeProdutos();
			int numPaginas = (int) Math.ceil((double) quantProdutos / PRODUTO_POR_PAGINA);
			String json = new Gson().toJson(numPaginas);
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.setStatus(200);		    
		    response.getWriter().write(json);
			response.getWriter().flush();
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(500);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print("Problemas ao buscar numero de paginas.");
			response.getWriter().flush();
		}
					
		
		
	}

	private void alterarProduto(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		long idProduto = Long.parseLong(request.getParameter("idProduto"));
		String descricao = request.getParameter("descricao");
		int quantidade = Integer.parseInt(request.getParameter("quantidade"));
		double preco = Double.parseDouble(request.getParameter("preco"));
		
		Produto produto = new Produto();
		produto.setIdProduto(idProduto);
		produto.setDescricao(descricao);
		produto.setQuantidade(quantidade);
		produto.setPreco(preco);
				
		ProdutoDao produtoDao;
		try {
			produtoDao = new ProdutoDao();
			produtoDao.alterar(produto);
			String json = new Gson().toJson(produto);
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.setStatus(200);		    
		    response.getWriter().write(json);
			response.getWriter().flush();
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(500);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print("Problemas ao alterar o produto.");
			response.getWriter().flush();
		} 
		
	}
	
	private int defineDeslocamento(String direcao, int paginaCorrente) {
		int deslocamento = 0;
		if (direcao.equals("atras")) {
			deslocamento = ((paginaCorrente-1) * PRODUTO_POR_PAGINA) - PRODUTO_POR_PAGINA;			
		} else if (direcao.equals("proximo")) {
			deslocamento = (paginaCorrente * PRODUTO_POR_PAGINA);
		} else { 
			deslocamento = (paginaCorrente * PRODUTO_POR_PAGINA) - PRODUTO_POR_PAGINA;
		}
		return deslocamento;
	}
	
	private void paginarProduto(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		int corrente = Integer.parseInt(request.getParameter("corrente"));
		String direcao = request.getParameter("direcao");
		int deslocamento = defineDeslocamento(direcao, corrente);
		ProdutoDao produtoDao;
		try {			
			produtoDao = new ProdutoDao();			
			List<Produto> produtos = produtoDao.paginacaoProduto(PRODUTO_POR_PAGINA, deslocamento); 
			String json = new Gson().toJson(produtos);
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.setStatus(200);		    
		    response.getWriter().write(json);
			response.getWriter().flush();
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(500);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print("Problemas ao listar o produto.");
			response.getWriter().flush();
		} 
	}

	private void listarProdutos(HttpServletRequest request, HttpServletResponse response) {
		
		String destino =  "/admin/gerenciar_produto.jsp";
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(destino);
		ProdutoDao produtoDao;
		try {
			produtoDao = new ProdutoDao();
			
			int quantProdutos = produtoDao.quantidadeProdutos();
			int numPaginas = (int) Math.ceil((double) quantProdutos / PRODUTO_POR_PAGINA);			
			request.setAttribute("numPaginas", numPaginas);
			List<Produto> produtos = produtoDao.paginacaoProduto(PRODUTO_POR_PAGINA, 0); 
			request.setAttribute("produtos", produtos);		
			
			
			requestDispatcher.forward(request, response);
		} catch (SQLException | ServletException | IOException e) {			
			e.printStackTrace();
		}
	}
	
	private void apagarProduto(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		long idProduto = Long.parseLong(request.getParameter("idProduto"));
		
		Produto produto = new Produto();
		produto.setIdProduto(idProduto);
				
		ProdutoDao produtoDao;
		try {
			produtoDao = new ProdutoDao();
			produtoDao.apagar(produto);
			response.setStatus(200);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().flush();
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(500);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print("Problemas ao remover o produto.");
			response.getWriter().flush();
		} 
		
	    
	}

	private void adicionarProduto(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String descricao = request.getParameter("descricao");
		int quantidade = Integer.parseInt(request.getParameter("quantidade"));
		double preco = Double.parseDouble(request.getParameter("preco"));
		
		Produto produto = new Produto();
		produto.setDescricao(descricao);
		produto.setQuantidade(quantidade);
		produto.setPreco(preco);
				
		ProdutoDao produtoDao;
		try {
			produtoDao = new ProdutoDao();
			produtoDao.adicionar(produto);
			String json = new Gson().toJson(produto);
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.setStatus(200);		    
		    response.getWriter().write(json);
			response.getWriter().flush();
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(500);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print("Problemas ao salvar o produto.");
			response.getWriter().flush();
		} 
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processarRequisicao(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processarRequisicao(request, response);
	}

}


