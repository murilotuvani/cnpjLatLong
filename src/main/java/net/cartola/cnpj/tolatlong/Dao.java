package net.cartola.cnpj.tolatlong;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import javax.sql.rowset.serial.SerialBlob;

/**
 * 27/02/2020 23:49:32
 *
 * @author murilotuvani
 */
public class Dao {

    protected Integer getIntOrNull(ResultSet rs, String string) throws SQLException {
        int value = rs.getInt(string);
        if (rs.wasNull()) {
            return null;
        }
        return value;
    }

    protected Long getLongOrNull(ResultSet rs, String string) throws SQLException {
        long value = rs.getLong(string);
        if (rs.wasNull()) {
            return null;
        }
        return value;
    }

    protected Float getFloatOrNull(ResultSet rs, String string) throws SQLException {
        Float value = rs.getFloat(string);
        if (rs.wasNull()) {
            return null;
        }
        return value;
    }
    
    protected BigDecimal getBigDecimalOrNull(ResultSet rs, String string) throws SQLException {
        BigDecimal value = rs.getBigDecimal(string);
        if (rs.wasNull()) {
            return null;
        }
        return value;
    }

    protected Double getDoubleOrNull(ResultSet rs, String string) throws SQLException {
        Double value = rs.getDouble(string);
        if (rs.wasNull()) {
            return null;
        }
        return value;
    }

    protected String getStringOrNull(ResultSet rs, String string) throws SQLException {
        String value = rs.getString(string);
        if (rs.wasNull()) {
            return null;
        }
        return value;
    }

    protected Date getDateOrNull(ResultSet rs, String string) throws SQLException {
        boolean isZeroDate = verifyZeroDate(rs, string);
        if (isZeroDate) {
            return null;
        }
        Date value = rs.getDate(string);
        if (rs.wasNull()) {
            return null;
        }
        return value;
    }

    protected Date getTimestampOrNull(ResultSet rs, String string) throws SQLException {
        boolean isZeroDate = verifyZeroDate(rs, string);
        if (isZeroDate) {
            return null;
        }
        Date value = rs.getTimestamp(string);
        if (rs.wasNull()) {
            return null;
        }
        return value;
    }

    protected LocalDateTime getLocalDateTimeOrNull(ResultSet rs, String string) throws SQLException {
        boolean isZeroDate = verifyZeroDate(rs, string);
        if (isZeroDate) {
            return null;
        }
        if (rs.wasNull()) {
            return null;
        }
        LocalDateTime value = rs.getTimestamp(string).toLocalDateTime();
        return value;
    }

    protected java.time.LocalDate getLocalDateOrNull(ResultSet rs, String string) throws SQLException {
        boolean isZeroDate = verifyZeroDate(rs, string);
        if (isZeroDate) {
            return null;
        }
        if (rs.wasNull()) {
            return null;
        }
        java.time.LocalDate value = rs.getDate(string).toLocalDate();
        return value;
    }

    protected LocalTime getLocalTimeOrNull(ResultSet rs, String string) throws SQLException {
        boolean isZeroDate = verifyZeroDate(rs, string);
        if (isZeroDate) {
            return null;
        }
        if (rs.wasNull()) {
            return null;
        }
        LocalTime value = rs.getTime(string).toLocalTime();
        return value;
    }

    private boolean verifyZeroDate(ResultSet rs, String string) throws SQLException {
        String verify = rs.getString(string);
        if (verify != null && verify.contains("0000-00-00")) {
            return true;
        }
        return false;
    }

    protected Time getTimeOrNull(ResultSet rs, String string) throws SQLException {
        Time value = rs.getTime(string);
        if (rs.wasNull()) {
            return null;
        }
        return value;
    }

    protected Boolean getBooleanOrNull(ResultSet rs, String string) throws SQLException {
        Boolean value = rs.getBoolean(string);
        if (rs.wasNull()) {
            value = null;
        }
        return value;
    }
    
    protected UUID getUUID(ResultSet rs, String column) throws SQLException {
        byte[] bytes = rs.getBytes(column);
        UUID value = null;
        if (!rs.wasNull()) {
            value = getUUIDFromBytes(bytes);
        }
        return value;
    }
    
    private UUID getUUIDFromBytes(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        Long high = byteBuffer.getLong();
        Long low = byteBuffer.getLong();

        return new UUID(high, low);
    }

    protected void setNullSafe(PreparedStatement stmt, Integer value, int index) throws SQLException {
        if (value == null || value == 0) {
            stmt.setNull(index, java.sql.Types.INTEGER);
        } else {
            stmt.setInt(index, value);
        }
    }

    protected void setNullSafe(PreparedStatement stmt, Long value, int index) throws SQLException {
        if (value == null || value == 0) {
            stmt.setNull(index, java.sql.Types.BIGINT);
        } else {
            stmt.setLong(index, value);
        }
    }

    protected void setNullSafe(PreparedStatement stmt, String value, int index) throws SQLException {
        if (isNull(value)) {
            stmt.setNull(index, java.sql.Types.VARCHAR);
        } else {
            stmt.setString(index, value);
        }
    }

    protected void setNullSafe(PreparedStatement stmt, BigDecimal value, int index) throws SQLException {
        if (value == null) {
            stmt.setNull(index, java.sql.Types.DECIMAL);
        } else {
            stmt.setBigDecimal(index, value);
        }
    }
    
    protected void setNullSafe(PreparedStatement stmt, Double value, int index) throws SQLException {
        if (value == null) {
            stmt.setNull(index, java.sql.Types.DOUBLE);
        } else {
            stmt.setDouble(index, value);
        }
    }

    protected void setNullSafe(PreparedStatement stmt, Float value, int index) throws SQLException {
        if (value == null || value == 0) {
            stmt.setNull(index, java.sql.Types.DOUBLE);
        } else {
            stmt.setDouble(index, value);
        }
    }

    protected void setNullSafe(PreparedStatement stmt, java.util.Date date, int idx) throws SQLException {
        if (date == null) {
            stmt.setNull(idx++, java.sql.Types.TIMESTAMP);
        } else {
            stmt.setTimestamp(idx++, new java.sql.Timestamp(date.getTime()));
        }
    }

    protected void setNullSafe(PreparedStatement stmt, Boolean value, int index) throws SQLException {
        if (value != null) {
            stmt.setBoolean(index, value);
        } else {
            stmt.setNull(index, java.sql.Types.BOOLEAN);
        }
    }

    protected void setNullSafe(PreparedStatement stmt, byte[] value, int index) throws SQLException {
        if (value != null) {
            stmt.setBlob(index, new SerialBlob(value));
        } else {
            stmt.setNull(index, java.sql.Types.BLOB);
        }
    }
    
    protected void setNullSafe(PreparedStatement stmt, UUID uuid, int index) throws SQLException {
        if (uuid != null) {
            setNullSafe(stmt, getBytesFromUUID(uuid), index);
        } else {
            stmt.setNull(index, java.sql.Types.BLOB);
        }
        
    }
    
    private byte[] getBytesFromUUID(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());

        return bb.array();
    }

    private boolean isNull(String value) {
        return (value == null || "".equals(value.trim()));
    }
}
