package subte.dao.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;

import subte.conexion.BDConexion;
import subte.dao.EstacionDAO;
import subte.dao.TramoDAO;
import subte.dao.secuencial.EstacionSecuencialDAO;
import subte.modelo.Estacion;
import subte.modelo.Tramo;

public class TramoPostgresqlDAO implements TramoDAO {


	private Hashtable<String, Estacion> estaciones;
	
	public TramoPostgresqlDAO() {
		estaciones = cargarEstaciones();
	}
	
	@Override
	public void insertar(Tramo tramo) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql="";
			sql+="INSERT INTO public.tramos (codigo_estacion1, codigo_estacion2, tiempo, congestion) ";
			sql+="VALUES(?,?,?,?) ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, tramo.getEstacion1().getCodigo());
			pstm.setString(2, tramo.getEstacion2().getCodigo());
			pstm.setInt(3, tramo.getTiempo());
			pstm.setInt(4, tramo.getCongestion());
			pstm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstm != null)
					pstm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
	}

	@Override
	public void actualizar(Tramo tramo) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql="UPDATE public.tramos ";
			sql+="SET tiempo = ?, congestion = ? ";
			sql+="WHERE codigo_estacion1 = ? AND codigo_estacion2 = ? ";
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, tramo.getTiempo());
			pstm.setInt(2, tramo.getCongestion());
			pstm.setString(3, tramo.getEstacion1().getCodigo());
			pstm.setString(4, tramo.getEstacion2().getCodigo());
			pstm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstm != null)
					pstm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}	
	}

	@Override
	public void borrar(Tramo tramo) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql="";
			sql+="DELETE FROM public.tramos WHERE codigo_estacion1 = ? AND codigo_estacion2 = ? ";		
			pstm = con.prepareStatement(sql);		
			pstm.setString(1, tramo.getEstacion1().getCodigo());
			pstm.setString(2, tramo.getEstacion2().getCodigo());
			pstm.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstm != null)
					pstm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
	}

	@Override
	public List<Tramo> buscarTodos() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "SELECT codigo_estacion1, codigo_estacion2, tiempo, congestion FROM public.tramos ";
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();
			List<Tramo> ret = new ArrayList<Tramo>();
			while (rs.next()) {
				Tramo t = new Tramo();
				t.setEstacion1(estaciones.get(rs.getString("codigo_estacion1")));
				t.setEstacion2(estaciones.get(rs.getString("codigo_estacion2")));
				t.setTiempo(rs.getInt("tiempo"));
				t.setCongestion(rs.getInt("congestion"));
				ret.add(t);
			}
			return ret;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstm != null)
					pstm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
	}
	
	private Hashtable<String, Estacion> cargarEstaciones() {
		Hashtable<String, Estacion> estaciones = new Hashtable<String, Estacion>();
		EstacionDAO estacionDAO = new EstacionPostgresqlDAO();
		List<Estacion> ds = estacionDAO.buscarTodos();
		for (Estacion d : ds)
			estaciones.put(d.getCodigo(), d);
		return estaciones;
	}
}
