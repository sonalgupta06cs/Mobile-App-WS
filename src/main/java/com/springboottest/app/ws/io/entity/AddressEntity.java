package com.appsdevelopeblog.app.ws.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="addresses")
public class AddressEntity implements Serializable {

	private static final long serialVersionUID = -4548580172612765118L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(length=30, nullable=false)
	private String addressId;
	@Column(length=15, nullable=false)
	private String city;
	@Column(length=15, nullable=false)
	private String country;
	@Column(length=100, nullable=false)
	private String streetName;
	@Column(length=7, nullable=false)
	private String postalCode;
	@Column(length=10, nullable=false)
	private String type;

    @ManyToOne
    @JoinColumn(name="Users_id")
	private UserEntity userDetails;

}
