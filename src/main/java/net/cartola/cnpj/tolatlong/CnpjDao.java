package net.cartola.cnpj.tolatlong;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CnpjDao extends Dao {

    private final Connection conn;

    public CnpjDao(Connection conn) {
        this.conn = conn;
    }

    public String getSelect() {
        return "SELECT C.cnpj_id,C.cnae,C.cnpj\n"
                + "     , C.tipo_logradouro,C.logradouro,C.numero,C.complemento\n"
                + "     , C.bairro,C.municipio,C.uf,C.cep,C.latitude,C.longitude\n"
                + "     , C.place_id\n"
                + "  FROM cnpj C";
    }

    public String getUpdate() {
        return "UPDATE cnpj SET latitude=?,longitude=?,place_id=? WHERE cnpj_id=?";
    }

    protected void prepareUpdate(PreparedStatement stmt, Cnpj c) throws SQLException {
        int idx = 1;
        setNullSafe(stmt, c.getLatitude(), idx++);
        setNullSafe(stmt, c.getLongitude(), idx++);
        setNullSafe(stmt, c.getPlaceId(), idx++);
        stmt.setLong(idx++, c.getCnpjId());
    }

    protected void setValues(Cnpj c, ResultSet rs) throws SQLException {
        c.setCnpjId(rs.getInt("cnpj_id"));
        c.setBairro(getStringOrNull(rs, "bairro"));
        c.setCep(rs.getInt("cep"));
        c.setCnae(rs.getInt("cnae"));
        c.setCnpj(rs.getLong("cnpj"));
        c.setComplemento(getStringOrNull(rs, "complemento"));
        c.setLogradouro(getStringOrNull(rs, "logradouro"));
        c.setMunicipio(getStringOrNull(rs, "municipio"));
        c.setNumero(getStringOrNull(rs, "numero"));
        c.setTipoLogradouro(getStringOrNull(rs, "tipo_logradouro"));
        c.setUf(getStringOrNull(rs, "uf"));
        c.setLatitude(getBigDecimalOrNull(rs, "latitude"));
        c.setLongitude(getBigDecimalOrNull(rs, "longitude"));
        c.setPlaceId(getStringOrNull(rs, "place_id"));
    }

    public List<Cnpj> listar(String where) throws SQLException {
        String query = getSelect() + where;
        System.out.println(query);

        List<Cnpj> lista = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Cnpj e = newInstance();
                setValues(e, rs);
                lista.add(e);
            }
        }
        return lista;
    }

    public int update(Cnpj cnpj) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(getUpdate());
        prepareUpdate(stmt, cnpj);
        int updateds = stmt.executeUpdate();
        return updateds;
    }

    public int update(List<Cnpj> cnpjs) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(getUpdate());
        for (Cnpj cnpj : cnpjs) {
            prepareUpdate(stmt, cnpj);
            stmt.addBatch();
        }

        int updateds[] = stmt.executeBatch();
        int soma = 0;
        for (int i : updateds) {
            soma += i;
        }
        return soma;
    }

    protected Cnpj newInstance() {
        return new Cnpj();
    }
}
