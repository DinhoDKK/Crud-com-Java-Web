package br.com.projetousuarios.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.gson.Gson;

import br.com.projetousuarios.dao.UsuarioDAO;
import br.com.projetousuarios.model.Usuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet("/UsuarioController")
public class UsuarioController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UsuarioController() {
        super();
    }
    
    private void processarRequisicao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String action = request.getParameter("acao");
 
		if (action == null) {
			throw new ServletException("Sem ação especificada.");
		} else if (action.equals("nova_conta")) {
			novaConta(request, response);		 
		} else if (action.equals("criar_conta")) {
			criarConta(request, response);
		} else if (action.equals("encaminhar_login")) { 
			encaminharLogin(request, response);
		} else if (action.equals("login")) {
			login(request, response);
		} else if (action.equals("logout")) {
			logout(request, response);
		}
	}


	private void encaminharLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String destino =  "index.jsp";
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(destino);		
		try {			
			requestDispatcher.forward(request, response);
		} catch (ServletException | IOException e) {			
			e.printStackTrace();
		}		
	}

	private void novaConta(HttpServletRequest request, HttpServletResponse response) {
		String destino =  "criar_conta.jsp";
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(destino);		
		try {			
			requestDispatcher.forward(request, response);
		} catch (ServletException | IOException e) {			
			e.printStackTrace();
		}		
	}
	
	private void logout(HttpServletRequest request, HttpServletResponse response)  throws IOException  {
		
		HttpSession session = request.getSession(false);
    	if(session != null){
    		session.invalidate();
    	}
    	//encaminha para a página inicial
    	response.sendRedirect(request.getContextPath() + "/index.jsp");
	}
	private void login(HttpServletRequest request, HttpServletResponse response)  throws IOException  {
		
		String email = request.getParameter("email");
		String senha = request.getParameter("senha");
		
		Usuario usuario = new Usuario();
		usuario.setEmail(email);
		String strMd5 = md5(senha);
		usuario.setSenha(strMd5);
				
		UsuarioDAO usuarioDAO;
		try {
			usuarioDAO = new UsuarioDAO();
			String json = null;
			if(usuarioDAO.login(usuario)) {
				HttpSession sessionVelha = request.getSession(false);
	            if (sessionVelha != null) {
	            	sessionVelha.invalidate();
	            }
	            //gera uma nova seção
	            HttpSession sessionNova = request.getSession(true);
	            sessionNova.setAttribute("usuario", usuario);
				//Expirar em 5 minutos sem atividade 
	            sessionNova.setMaxInactiveInterval(5*60);
				json = new Gson().toJson("OK");			
			}else {
				json = new Gson().toJson("NO");
			    
			}
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.setStatus(200);		    
		    response.getWriter().write(json);
			response.getWriter().flush(); 
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(500);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print("Problemas ao criar usuário.");
			response.getWriter().flush();
		} 		
	}
	
	private String md5(String str) {
		String strMd5 = null;		
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(str.getBytes(),0,str.length());
		    strMd5 = new BigInteger(1,m.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	    
	    return strMd5;
	}

	private void criarConta(HttpServletRequest request, HttpServletResponse response)  throws IOException  {
		
		String nome = request.getParameter("nome");
		String sobreNome = request.getParameter("sobrenome");
		String email = request.getParameter("email");
		String senha = request.getParameter("senha");
		
		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		usuario.setSobreNome(sobreNome);
		usuario.setEmail(email);
		String strMd5 = md5(senha);
		usuario.setSenha(strMd5);
				
		UsuarioDAO usuarioDAO;
		try {
			usuarioDAO = new UsuarioDAO();
			usuarioDAO.adicionar(usuario);
			String json = new Gson().toJson("OK");
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.setStatus(200);		    
		    response.getWriter().write(json);
			response.getWriter().flush(); 
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(500);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print("Problemas ao criar usuário.");
			response.getWriter().flush();
		} 		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processarRequisicao(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processarRequisicao(request, response);
	}

}
