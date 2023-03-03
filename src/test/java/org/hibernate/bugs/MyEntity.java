package org.hibernate.bugs;

import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.annotations.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MY_TABLE")
public class MyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "SORTED_IDS")
	@Type(org.hibernate.bugs.IntegerSetUserType.class)
	private final SortedSet<Integer> sortedIds = new TreeSet<>();

	@Column(name = "SORTED_IDS", updatable = false, insertable = false)
	private String sortedIdsExpression;

	public SortedSet<Integer> getSortedIds() {
		return sortedIds;
	}

}