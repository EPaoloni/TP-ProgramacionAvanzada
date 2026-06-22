/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import exceptions.NombreApellidoInvalidoException;
import exceptions.PromedioInvalidoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persona.Alumno;
import utils.DateUtils;

/**
 *
 * @author g.guzman
 */
public class AlumnoDAOSQL extends DAO<Alumno,Integer> {

    private final Connection connection;
    private final PreparedStatement insertPrepareStatement;
    private final PreparedStatement readPrepareStatement;
    private final PreparedStatement existsPrepareStatement;
    private final PreparedStatement updatePrepareStatement;
    private final PreparedStatement deletePrepareStatement;

    
    AlumnoDAOSQL(String user, String password) throws DAOException {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/universidad", user, password);
            System.out.println("dao.AlumnoDAOSQL.<init>() OK!!!");

            String insertSql = "INSERT INTO alumnos\n" +
                        "(DNI, NOMBRE, APELLIDO, FECNAC, FECING, PROMEDIO, CANTMATAPROB, ESTADO)\n" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            insertPrepareStatement = connection.prepareStatement(insertSql);

            String updateSql = """
                               UPDATE alumnos SET
                               NOMBRE = ?,
                               APELLIDO = ?,
                               FECNAC = ?,
                               FECING = ?,
                               PROMEDIO = ?,
                               CANTMATAPROB = ?,
                               ESTADO = ?
                               WHERE DNI = ?
                               """;
            updatePrepareStatement = connection.prepareStatement(updateSql);
            
            String deleteSql = """ 
                               UPDATE alumnos SET 
                               ESTADO = 'B'
                               WHERE DNI = ? 
                               """;
            deletePrepareStatement = connection.prepareStatement(deleteSql);
            
            String readSql = "SELECT * FROM alumnos WHERE DNI = ?";
            readPrepareStatement = connection.prepareStatement(readSql);
            
            
            String existsSql = "SELECT COUNT(DNI) FROM UNIVERSIDAD.ALUMNOS WHERE DNI = ?";
            existsPrepareStatement = connection.prepareStatement(existsSql);
            
        } catch (SQLException ex) {
            Logger.getLogger(AlumnoDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error I/O: "+ex.getLocalizedMessage());
        }
        
    }
    
    @Override
    public void create(Alumno alumno) throws DAOException {
        try {
            int index = 1;
            insertPrepareStatement.setInt(index++, alumno.getDni());
            insertPrepareStatement.setString(index++, alumno.getNombre());
            insertPrepareStatement.setString(index++, alumno.getApellido());
            insertPrepareStatement.setDate(index++, DateUtils.localDate2SqlDate(alumno.getFecNac()));
            insertPrepareStatement.setDate(index++, DateUtils.localDate2SqlDate(alumno.getFecIng()));
            insertPrepareStatement.setDouble(index++, alumno.getPromedio());
            insertPrepareStatement.setShort(index++, alumno.getCantMatAprob());
            insertPrepareStatement.setString(index++, "A");

            insertPrepareStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(AlumnoDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error AL INSERTAR: "+ex.getLocalizedMessage());
        }
    }

    @Override
    public Alumno read(Integer dni) throws DAOException {
        try {
            readPrepareStatement.setInt(1, dni);
            final ResultSet rs = readPrepareStatement.executeQuery();
            if (rs.next()) {
                Alumno alu = new Alumno();
                alu.setDni(rs.getInt("DNI"));
                alu.setNombre(rs.getString("NOMBRE"));
                alu.setApellido(rs.getString("APELLIDO"));
                alu.setFecNac(DateUtils.sqlDate2LocalDate(rs.getDate("FECNAC")));
                alu.setFecIng(DateUtils.sqlDate2LocalDate(rs.getDate("FECING")));
                alu.setPromedio(rs.getDouble("PROMEDIO"));
                alu.setCantMatAprob(rs.getShort("CANTMATAPROB"));
                alu.setEstado(rs.getString("ESTADO").charAt(0));

                return alu;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AlumnoDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error AL LEER: "+ex.getLocalizedMessage());
        } catch (NombreApellidoInvalidoException ex) {
            Logger.getLogger(AlumnoDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Erro al setear datos del alumno: "+ex.getLocalizedMessage());
        } catch (PromedioInvalidoException ex) {
            Logger.getLogger(AlumnoDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    @Override
    public void update(Alumno entidad) throws DAOException {
        try {
            int index = 1;
            updatePrepareStatement.setString(index++, entidad.getNombre());
            updatePrepareStatement.setString(index++, entidad.getApellido());
            updatePrepareStatement.setDate(index++, DateUtils.localDate2SqlDate(entidad.getFecNac()));
            updatePrepareStatement.setDate(index++, DateUtils.localDate2SqlDate(entidad.getFecIng()));
            updatePrepareStatement.setDouble(index++, entidad.getPromedio());
            updatePrepareStatement.setShort(index++, entidad.getCantMatAprob());
            updatePrepareStatement.setString(index++, String.valueOf(entidad.getEstado()));
            updatePrepareStatement.setInt(index++, entidad.getDni());

            updatePrepareStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(AlumnoDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error AL ACTUALIZAR: "+ex.getLocalizedMessage());
        }
    }

    @Override
    public void delete(Integer id) throws DAOException {
        try {
            deletePrepareStatement.setInt(1, id);

            deletePrepareStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(AlumnoDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error AL ELIMINAR: "+ex.getLocalizedMessage());
        }
    }

    @Override
    public List<Alumno> findAll(boolean all) throws DAOException {
        String findAllSQL = " SELECT * FROM alumnos ";
        List<Alumno> listaAlumnos = new ArrayList<>();
        try{
            if(!all)
                findAllSQL += " WHERE ESTADO <> 'B' ";
            
            final ResultSet rs = connection.prepareStatement(findAllSQL).executeQuery();
            while (rs.next()) {
                Alumno alu = new Alumno();
                alu.setDni(rs.getInt("DNI"));
                alu.setNombre(rs.getString("NOMBRE"));
                alu.setApellido(rs.getString("APELLIDO"));
                alu.setFecNac(DateUtils.sqlDate2LocalDate(rs.getDate("FECNAC")));
                alu.setFecIng(DateUtils.sqlDate2LocalDate(rs.getDate("FECING")));
                alu.setPromedio(rs.getDouble("PROMEDIO"));
                alu.setCantMatAprob(rs.getShort("CANTMATAPROB"));
                alu.setEstado(rs.getString("ESTADO").charAt(0));

                listaAlumnos.add(alu);
            }
            
            return listaAlumnos;
        } catch (SQLException ex) {
            Logger.getLogger(AlumnoDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error AL LEER: "+ex.getLocalizedMessage());
        } catch (NombreApellidoInvalidoException | PromedioInvalidoException ex) {
            Logger.getLogger(AlumnoDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Erro al setear datos del alumno: "+ex.getLocalizedMessage());
        }
    }

    @Override
    public void close() throws DAOException {
        try {
            if(connection!=null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(AlumnoDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Erro al cerrar la conexión:" + ex.getLocalizedMessage());
        }
        
    }

    @Override
    public boolean exist(Integer id) throws DAOException {
        try{
            existsPrepareStatement.setInt(1, id);
            
            final ResultSet rs = existsPrepareStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("COUNT(DNI)") == 1;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AlumnoDAOSQL.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Error AL consultar si existe: "+ex.getLocalizedMessage());
        }
    }
    
}
