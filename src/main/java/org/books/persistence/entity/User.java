package org.books.persistence.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "BookstoreUser", uniqueConstraints =
		{ @UniqueConstraint(columnNames = { "name" }) })
public class User extends BaseEntity {

	@NotNull
	private String name;
	private String password;

	/**
	 * Type of "Set" because unique required but not ordered
	 */
	@ManyToMany(mappedBy = "users")
	private Set<Group> groups = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	public void addGroup(Group group) {
		groups.add(group);
		group.getUsers().add(this);
	}

	@PrePersist
	public void prePersist() {
		if (groups == null || groups.isEmpty()) {
			throw new IllegalArgumentException("User must have at least one group");
		}
	}
}