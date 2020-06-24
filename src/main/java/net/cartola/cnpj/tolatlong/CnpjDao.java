package net.cartola.cnpj.tolatlong;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CnpjDao extends Dao {

    public String getSelect() {
        return "SELECT C.cnpj_id,C.cnae,C.cnpj\n"
             + "     , C.tipo_logradouro,C.logradouro,C.numero,C.complemento\n"
             + "     , C.bairro,C.municipio,C.uf,C.cep,C.latitude,C.longitude\n"
             + "  FROM cnpj C";
    }

    public String getUpdate() {
        return "UPDATE cnpj SET latitude=?,longitude=? WHERE cnpj_id=?";
    }

    protected void prepareUpdate(PreparedStatement stmt, Cnpj c) throws SQLException {
        int idx = 1;
        setNullSafe(stmt, c.getLatitude(), idx++);
        setNullSafe(stmt, c.getLongitude(), idx++);
        stmt.setLong(idx++, c.getCnpjId());
    }

    protected void setValues(Cnpj c, ResultSet rs) throws SQLException {
        c.setCnpjId(rs.getInt("cnpj_id"));
        c.setBairro(getStringOrNull(rs, "bairro"));
        c.setCep(rs.getInt("cep"));
        c.setCnae(rs.getInt("cnae"));
        c.setCnpj(rs.getInt("cnpj"));
        c.setComplemento(getStringOrNull(rs, "complemento"));
        c.setLogradouro(getStringOrNull(rs, "logradouro"));
        c.setMunicipio(getStringOrNull(rs, "municipio"));
        c.setNumero(getStringOrNull(rs, "numero"));
        c.setTipoLogradouro(getStringOrNull(rs, "tipo_logradouro"));
        c.setUf(getStringOrNull(rs, "uf"));
        c.setLatitude(getBigDecimalOrNull(rs, "latitude"));
        c.setLongitude(getBigDecimalOrNull(rs, "longitude"));
    }

    protected Cnpj newInstance() {
        return new Cnpj();
    }
}
