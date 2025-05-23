package com.algaworks.brewer.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Email;

import com.algaworks.brewer.validation.AtributoConfirmacao;

@Entity
@Table(name = "usuario")
@AtributoConfirmacao(atributo = "senha", atributoConfirmacao="confirmacaoSenha", message = "Senhas não conferem")
@DynamicUpdate
public class Usuario implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	@NotBlank(message = "Nome é obrigatório")
	private String nome;
	@NotBlank(message = "Email é obrigatório")
	@Email(message = "Email inválido!")
	private String email;
	//Está atrapalhando a edição
	//@Size(max = 100, min = 6, message = "Senha deve ser maior que 6 digitos")
	private String senha;
	//@NotBlank(message = "Status é obrigatório")
	private Boolean ativo;
	@NotNull(message = "Data de Nascimento é obrigatória")
	@Column(name = "data_nascimento")
	private LocalDate dataNascimento;
	
	//@NotNull(message = "Selecione pelo menos um grupo")
	@Size(min = 1, message = "Selecione pelo menos um grupo")
	@ManyToMany//(fetch = FetchType.EAGER)
	@JoinTable( name = "usuario_grupo", joinColumns = @JoinColumn(name = "codigo_usuario")
	                                  , inverseJoinColumns = @JoinColumn(name = "codigo_grupo"))
	private List<Grupo> grupos;	
	
	@PreUpdate
	private void preUpdate() {
		this.confirmacaoSenha = senha;
	}
	
	@Transient
	private String confirmacaoSenha;
	
	public String getConfirmacaoSenha() {
		return confirmacaoSenha;
	}

	public void setConfirmacaoSenha(String confirmacaoSenha) {
		this.confirmacaoSenha = confirmacaoSenha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}
	
	public boolean isNovo() {
		return codigo == null;
	}
	
	public boolean isEdicao() {
		return codigo != null;
	}
	
}
