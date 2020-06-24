package net.cartola.cnpj.tolatlong;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author murilotuvani
 */
public class CnpjDaoTest {

    private static ConnectionFactory connFactory;
    
    public CnpjDaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        CnpjLatLongConfig config = new CnpjLatLongConfig();
        config.setBdHost("localhost");
        config.setBdBancoDados("cnpj");
        config.setBdUsuario("root");
        config.setBdSenha("root");
        connFactory = new ConnectionFactory(config);
    }
    
    @AfterClass
    public static void tearDownClass() {
        
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getSelect method, of class CnpjDao.
     */
    @Test
    public void testList() throws SQLException {
        Connection conn = connFactory.getConnection();
        CnpjDao instance = new CnpjDao(conn);
        long cnpj = 5437537000137L;
        List<Cnpj> cnpjs = instance.listar("\n WHERE cnpj="+cnpj);
        assertEquals(1, cnpjs.size());
        assertEquals(cnpj, cnpjs.get(0).getCnpj());
    }
    
    // LATITUDE: -23.25638008
    // LONGITUDE: -47.30302811
    
    @Test
    public void testUpdate() throws SQLException {
        Connection conn = connFactory.getConnection();
        CnpjDao instance = new CnpjDao(conn);
        long cnpj = 5437537000137L;
        List<Cnpj> cnpjs = instance.listar("\n WHERE cnpj="+cnpj);
        Cnpj ocnpj = cnpjs.get(0);
        assertEquals(1, cnpjs.size());
        assertEquals(cnpj, ocnpj.getCnpj());
        
        BigDecimal lat = new BigDecimal("-23.25638008");
        BigDecimal lng = new BigDecimal("-47.30302811");
        ocnpj.setLatitude(lat);
        ocnpj.setLongitude(lng);
        assertEquals(1, instance.update(ocnpj));
        
        cnpjs = instance.listar("\n WHERE cnpj="+cnpj);
        ocnpj = cnpjs.get(0);
        assertEquals(lat, ocnpj.getLatitude());
        assertEquals(lng, ocnpj.getLongitude());
        
        
        
        
    }

    
}
