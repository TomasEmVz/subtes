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
import subte.dao.LineaDAO;
import subte.modelo.Estacion;
import subte.modelo.Linea;

public class EstacionPostgresqlDAO implements EstacionDAO {


	private Hashtable<String, Linea> lineas;
	
	public EstacionPostgresqlDAO() {
		lineas = cargarLineas();
	}
	
	@Override
	public void insertar(Estacion estacion) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql="";
			sql+="INSERT INTO public.estaciones (codigo, nombre, codigo_linea) ";
			sql+="VALUES(?,?,?) ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, estacion.getCodigo());
			pstm.setString(2, estacion.getNombre());
			pstm.setString(3, estacion.getLinea().getCodigo());
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
	public void actualizar(Estacion estacion) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql="UPDATE public.estaciones ";
			sql+="SET nombre = ?, codigo_linea = ? ";
			sql+="WHERE codigo = ? ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, estacion.getNombre());
			pstm.setString(2, estacion.getLinea().getCodigo());
			pstm.setString(3, estacion.getCodigo());
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
	public void borrar(Estacion estacion) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql="";
			sql+="DELETE FROM public.estaciones WHERE codigo = ? ";		
			pstm = con.prepareStatement(sql);		
			pstm.setString(1, estacion.getCodigo());
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
	public List<Estacion> buscarTodos() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "SELECT codigo, nombre, codigo_linea FROM public.estaciones ";
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();
			List<Estacion> ret = new ArrayList<Estacion>();
			while (rs.next()) {
				ret.add(new Estacion(rs.getString("codigo"), rs.getString("nombre"), lineas.get(rs.getString("codigo_linea"))));		
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
	
	private Hashtable<String, Linea> cargarLineas() {
		Hashtable<String, Linea> lineas = new Hashtable<String, Linea>();
		LineaDAO lineaDAO = new LineaPostgresqlDAO();
		List<Linea> ds = lineaDAO.buscarTodos();
		for (Linea d : ds)
			lineas.put(d.getCodigo(), d);
		return lineas;
	}
}
