package com.herve;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;


@Entity
@Table(name = "ReqRep")
@NamedQuery(name = "ReqRep.findAll", query = "SELECT e FROM ReqRep e")
@NamedQuery(name = "ReqRep.findReqRep", query = "SELECT e FROM ReqRep e WHERE "
		+ "e.request = :request")
public class ReqRep implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "request", columnDefinition = "VARCHAR(512)")
	private String request;

	@Column(name = "response", columnDefinition = "VARCHAR(30000)" )
	private String response;
	

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}