package org.books.persistence.entity;

import org.books.persistence.enumeration.UserGroup;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USERLOGIN", uniqueConstraints =
		{ @UniqueConstraint(columnNames = { "userName" }) })
public class Login extends BaseEntity {

	@NotNull
	private String userName;
	private String password;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "USERGROUP")
	private UserGroup group;

	@Version
	private Long version;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserGroup getGroup() {
		return group;
	}

	public void setGroup(UserGroup group) {
		this.group = group;
	}
}