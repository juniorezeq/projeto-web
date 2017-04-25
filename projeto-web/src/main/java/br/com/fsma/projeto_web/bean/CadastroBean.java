package br.com.fsma.projeto_web.bean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

//import javax.enterprise.context.ApplicationScoped;
//import javax.faces.view.ViewScoped;
//import javax.enterprise.context.RequestScoped;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.fsma.projeto_web.modelo.dao.UsuarioDao;
import br.com.fsma.projeto_web.modelo.negocio.Usuario;
import br.com.fsma.projeto_web.tx.Transacional;

@Named
@ViewScoped
public class CadastroBean implements Serializable {
	/**
	 * criado por Ezequiel
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioDao usuarioDao;
	private Usuario usuario = new Usuario();

	@PostConstruct
	public void init() {
		usuario = new Usuario();
	}

	@Transacional
	public String iniciarCadastro() {
		return "/view/cadastro/cadastro.xhtml?faces-redirect=true";
	}

	@Transacional
	public String voltar() {
		return "/view/menu/menu.xhtml?faces-redirect=true";
	}

	@Transacional
	public String incluir() {
		usuario.setDataDoCadastro(LocalDateTime.now());
		if (usuario.getLogin().isEmpty() || usuario.getSenha().isEmpty()) {
			String mensagem = "Todos os campos devem ser preenchidos";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!!", mensagem));
			return null;
		}
		boolean existe = usuarioDao.existeLogin(usuario.getLogin());
		if (existe == false) {
			usuarioDao.adiciona(usuario);
			System.out.println("init.Cadastro()");
			String mensagem = "Cadastrado com sucesso";
			usuario = new Usuario();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!!", mensagem));
			return null;
		}
		System.out.println("init.Cadastro()");
		String mensagem = "Não foi possivel realizar o cadastro usuário já existe";
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro!!", mensagem));

		return null;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Transacional
	public List<Usuario> usuariosCadastrados() {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		usuarios = usuarioDao.listaTodosPaginada(0, 100);
		return usuarios;
	}

	@Transacional
	public void atualizaUsuario() {
		try {
			usuarioDao.atualiza(usuario);
			String mensagem = "Alterado com sucesso";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!!", mensagem));

		} catch (Exception e) {
			String mensagem = "Erro não foi possivel atualizar";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", mensagem));

		}
	}

}
