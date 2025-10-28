package subte.dao.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import subte.conexion.BDConexion;
import subte.dao.LineaDAO;
import subte.modelo.Linea;

public class LineaPostgresqlDAO implements LineaDAO {

	@Override
	public void insertar(Linea linea) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql="";
			sql+="INSERT INTO public.lineas (codigo, nombre) ";
			sql+="VALUES(?,?) ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, linea.getCodigo());
			pstm.setString(2, linea.getNombre());	
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
	public void actualizar(Linea linea) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql="UPDATE public.lineas ";
			sql+="SET nombre = ? ";
			sql+="WHERE codigo = ? ";
			pstm = con.prepareStatement(sql);
			pstm.setString(1, linea.getNombre());
			pstm.setString(2, linea.getCodigo());
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
	public void borrar(Linea linea) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql="";
			sql+="DELETE FROM public.lineas WHERE codigo = ? ";		
			pstm = con.prepareStatement(sql);		
			pstm.setString(1, linea.getCodigo());
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
	public List<Linea> buscarTodos() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = BDConexion.getConnection();
			String sql = "SELECT codigo, nombre FROM public.lineas ";
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();
			List<Linea> ret = new ArrayList<Linea>();
			while (rs.next()) {
				ret.add(new Linea(rs.getString("codigo"), rs.getString("nombre")));		
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
}
