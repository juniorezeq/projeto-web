package br.com.fsma.projeto_web.modelo.negocio;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "tb_locacao")
public class Locacao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_locacao")
	private Long id;
	@Column(nullable = false, insertable = true, updatable = false)
	private LocalDateTime dataRegistro;
	@Column(nullable = false, insertable = true, updatable = false)
	private LocalDateTime dataInicio;
	@Column(nullable = false, insertable = true, updatable = true)
	private LocalDateTime dataFim;
	@Column(nullable = false, insertable = true, updatable = true)
	private Double valorDiaria;
	@Column(name = "valorTotal")
	private Double valorTotal;
	
	 @ManyToOne
	 @JoinColumn(name="id_cliente", referencedColumnName="id_cliente")
	 private Cliente cliente;
	
	@ManyToMany
	private List<Equipamento> equipamentos;
	
	
	
   
	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public List<Equipamento> getEquipamentos() {
		return equipamentos;
	}

	public void setEquipamentos(List<Equipamento> equipamentos) {
		this.equipamentos = equipamentos;
	}

	public LocalDateTime getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(LocalDateTime dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public LocalDateTime getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDateTime dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDateTime getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDateTime dataFim) {
		this.dataFim = dataFim;
	}

	public Double getValorDiaria() {
		return valorDiaria;
	}

	public void setValorDiaria(Double valorDiaria) {
		this.valorDiaria = valorDiaria;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Locação [id=" + id + ", cliente=" + cliente.getNome() + ", inicio=" + dataInicio + ", fim=" + dataFim + ", valorDiaria="
				+ valorDiaria + ", valorTotal=" + valorTotal + "]";
	}
	
	public void converterDataInicio(Date dataIni){
		Instant instant = Instant.ofEpochMilli(dataIni.getTime());
		dataInicio = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

	public void converterDataFim(Date dataFinal){
		Instant instant = Instant.ofEpochMilli(dataFinal.getTime());
		dataFim = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

}