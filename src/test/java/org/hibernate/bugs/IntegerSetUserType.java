package org.hibernate.bugs;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class IntegerSetUserType
		implements UserType<SortedSet<Integer>>, AttributeConverter<SortedSet<Integer>, String> {

	@Override
	public int getSqlType() {
		return Types.VARCHAR;
	}

	@Override
	public Class<SortedSet<Integer>> returnedClass() {
		return Class.class.cast(SortedSet.class);
	}

	@Override
	public boolean equals(SortedSet<Integer> x, SortedSet<Integer> y) throws HibernateException {
		return Objects.equals(x, y);
	}

	@Override
	public int hashCode(SortedSet<Integer> x) throws HibernateException {
		return Objects.hashCode(x);
	}

	@Override
	public SortedSet<Integer> nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session,
			Object owner) throws SQLException {
		String databaseValue = rs.wasNull() ? "" : rs.getString(position);

		return convertToEntityAttribute(databaseValue);
	}

	@Override
	public void nullSafeSet(PreparedStatement st, SortedSet<Integer> values, int index,
			SharedSessionContractImplementor session) throws SQLException {
		if (values == null || values.isEmpty()) {
			st.setNull(index, Types.VARCHAR);
			return;
		}

		String databaseValue = convertToDatabaseColumn(values);

		st.setString(index, databaseValue);
	}

	@Override
	public SortedSet<Integer> deepCopy(SortedSet<Integer> value) {
		return value;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(SortedSet<Integer> value) throws HibernateException {
		return (Serializable) value;
	}

	@Override
	public SortedSet<Integer> assemble(Serializable cached, Object owner) throws HibernateException {
		return (SortedSet<Integer>) cached;
	}

	@Override
	public SortedSet<Integer> replace(SortedSet<Integer> original, SortedSet<Integer> target, Object owner)
			throws HibernateException {
		return original;
	}

	@Override
	public String convertToDatabaseColumn(SortedSet<Integer> values) {
		return values.stream().map(Object::toString).collect(Collectors.joining("|", "|", "|"));
	}

	@Override
	public SortedSet<Integer> convertToEntityAttribute(String databaseValue) {
		return Arrays.stream(databaseValue.split("\\|")).map(value -> value.trim()).filter(value -> !value.isEmpty())
				.map(value -> Integer.valueOf(value)).collect(Collectors.toCollection(TreeSet::new));
	}

}
