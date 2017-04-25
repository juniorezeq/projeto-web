package br.com.fsma.projeto_web.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import br.com.fsma.projeto_web.modelo.dao.EquipamentoDao;
import br.com.fsma.projeto_web.modelo.negocio.Equipamento;
import br.com.fsma.projeto_web.modelo.negocio.Usuario;
import br.com.fsma.projeto_web.modelo.negocio.Equipamento.situacao;
import br.com.fsma.projeto_web.tx.Transacional;

@Named
@ViewScoped
public class EquipamentoBean implements Serializable {
	/**
	 * criado por Ezequiel
	 */
	private static final String USUARIO_LOGADO = "usuarioLogado";
	

	@Inject
	private HttpSession session;
	@Inject
	private EquipamentoDao equipamentoDao;
	private Equipamento equipamento = new Equipamento();
	private Usuario usuario;

	private static final long serialVersionUID = 1L;
    
	@PostConstruct
	public void init() {
		System.out.println("EquiapamentoBean.init();");
		usuario = (Usuario) session.getAttribute(USUARIO_LOGADO);
		if (usuario == null) {
			usuario = new Usuario();
		}
		equipamento = new Equipamento();
		equipamento.setSituacao(situacao.Disponivel);
	}


	@Transacional
	public String iniciarCadastroEquipamentos() {
		return "/view/cadastro/cadastroequipamento.xhtml?faces-redirect=true";
	}

	@Transacional
	public String alterarEquipamentos() {
		return "/view/alteracao/alteracaoequipamento.xhtml?faces-redirect=true";
	}

	@Transacional
	public String checarTag() {
		System.out.println(equipamento.getTag());
		Equipamento recebido = equipamentoDao.buscaPorTag(equipamento.getTag());
		if (recebido != null) {
			equipamento = recebido;
		} else {
			equipamento = new Equipamento();
			String mensagem = "Equipamento não foi encontrado verifique a TAG digitada";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", mensagem));

		}
		System.out.println(equipamento);
		return null;
	}

	@Transacional
	public String verificaEquipamento() {
		Equipamento recebido = equipamentoDao.buscaPorTag(equipamento.getTag());
		if (recebido != null) {
			System.out.println("ja existe tag:" + recebido.getTag());
			String mensagem = "A Tag digitada já foi cadastrada";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", mensagem));
			equipamento = new Equipamento();			
		} 
		return null;
	}
	
	
	@Transacional
	public String incluir() {
		if (equipamento.getTag().isEmpty() || equipamento.getDescricao().isEmpty()) {
			String mensagem = "Todos os campos devem ser preenchidos";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!!", mensagem));
			return null;
		}
		boolean existe = equipamentoDao.existeTag(equipamento.getTag());
		if (existe == false) {
			equipamentoDao.adiciona(equipamento);
			System.out.println("init.Cadastro()");
			String mensagem = "Cadastrado com sucesso";
			equipamento = new Equipamento();
			equipamento.setSituacao(situacao.Disponivel);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!!", mensagem));
			return null;
		}
		System.out.println("init.Cadastro()");
		String mensagem = "Esta Tag já esta associada a outro equipamento";
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!!", mensagem));
		equipamento = new Equipamento();
		equipamento.setSituacao(situacao.Disponivel);
		return null;
	}

	public Equipamento getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(Equipamento equipamento) {
		this.equipamento = equipamento;
	}

	@Transacional
	public String atualizaEquipamento() {
		try {
			equipamentoDao.atualiza(equipamento);
			String mensagem = "Atualizada Corretamente!";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", mensagem));

		} catch (Exception e) {
			String mensagem = "Não Atualizada";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", mensagem));
		}
		equipamento = new Equipamento();
		equipamento.setSituacao(situacao.Disponivel);
		return null;
	}

	@Transacional
	public String excluiEquipamento() {
		try {
			equipamentoDao.remove(equipamento);
			String mensagem = "Excluido corretamente!";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", mensagem));

		} catch (Exception e) {
			String mensagem = "Não foi possivel Excluir";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", mensagem));
		}
		equipamento = new Equipamento();
		equipamento.setSituacao(situacao.Disponivel);
		return null;
	}

	@Transacional
	public List<Equipamento> equipamentosCadastrados() {
		List<Equipamento> equipamentos = new ArrayList<Equipamento>();
		equipamentos = equipamentoDao.listaTodosPaginada(0, 100);
		return equipamentos;
	}

}
