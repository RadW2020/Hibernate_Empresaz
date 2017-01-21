package hibernate_empresaz;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import empresaz.entity.Departamentos;
import empresaz.entity.Empleados;
import empresaz.util.HibernateUtil;
import java.time.Instant;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author RadW
 */
public class Hibernate_Empresaz {
    public static Departamentos dep10;
    public static Departamentos dep11;
    public static Empleados emp;
    //Inicializacion del entorno Hibernate
    //Configuration cfg = new Configuration().configure();
    //crea el ejemplar de sessionFactory
    //SessionFactory sf = cfg.buildSessionFactory();
    //obtiene un objeto sesion
    //Session sesion = sf.openSession();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Hibernate_Empresaz.listadoDepartamentos();
        //Hibernate_Empresaz.insertaDepartamento((byte)66, "MARKETING", "PONTEVEDRA");
        //Hibernate_Empresaz.listadoDepartamentos();

        //obtener sesion y abrir
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session sesion = sf.openSession();
        /** PRUEBA METODO load() **/
        System.out.println("DATOS DEL DEPARTAMENTO 10");
        dep10 = new Departamentos();
        dep10 = (Departamentos)sesion.load(Departamentos.class, (byte)10);
        System.out.println("Nombre Dep: " + dep10.getDnombre());
        System.out.println("Localidad: " + dep10.getLoc());
        System.out.println("EMPLEADOS DEL DEPARTAMENTO 10");
        Set<Empleados>listaEmple = dep10.getEmpleadoses();
        Iterator<Empleados> it = listaEmple.iterator();
        System.out.println("Numero de Empleados: " + listaEmple.size());
        while (it.hasNext()){
            Empleados emple = new Empleados();
            emple = it.next();
            System.out.println(emple.getApellido() +" * "+ emple.getSalario());
        }
        
        /** PRUEBA METODO get() **/
        System.out.println("DATOS DEL DEPARTAMENTO 11");
        dep11 = (Departamentos) sesion.get(Departamentos.class, (byte)11);
        if (dep11 == null){
            System.out.println("El departamento no existe");
        } else {
            System.out.println("Nombre Dep: " + dep11. getDnombre());
            System.out.println("Localidad: " + dep11. getLoc());
        }
        
        
        /** insertar un nuevo empleado **/
        Empleados empPepe; // Nuevo empleado identificador:4465, de nombre PEPE, oficio VENDEDOR y en el departamento 10
        empPepe = new Empleados((short)4465,dep10,"PEPE","VENDEDOR",(short) 10, Date.from(Instant.now()),(float)1200.00,(float)10.00);
        
        
        insertaEmp(empPepe); // inserta a Pepe en la tabla empleados
        modSalarioyComision((short)4465, (float)2200.00, (float)10.00); //Ejemplo de uso de modificación de un empleado en la base de datos
        
        borraEmp( (short)4465 ); //borra un empleado de la BD que tenga el numero identificador dado
          
        
        
        
        
        sesion.close();
    }// FIN main
    
    /**
     * Método para lanzar consultas
     */
    
    /**
     * inserta in objeto empleado en la base de datos
     */
    public static void insertaEmp(Empleados emp){
        Session sesion = null;
        try{
            //obtiene una sesion
            SessionFactory sf = HibernateUtil.getSessionFactory();
            //abre la sesion
            sesion = sf.openSession();
            //comienza una transaccion
            Transaction tx = sesion.beginTransaction();
            System.out.println("Inserta un empleado en la tabla Empleados");
            sesion.save(emp);
            tx.commit();            
        } catch (HibernateException hex){
            System.err.println("Error " + hex.getCause() + ", " + hex.getMessage());
        } finally {
            sesion.close();
        }
        
    }
    /**
     * modifica el salario y comisión de un empleado dado su numero de identificador
     * @param emp 
     */
    public static void modSalarioyComision(short empNo, float sal, float com){
        Session sesion = null;
        try{
            //obtiene una sesión
            SessionFactory sf = HibernateUtil.getSessionFactory();
            //abre la sesión
            sesion = sf.openSession();
            //comienza una transacción
            Transaction tx = sesion.beginTransaction();
            System.out.println("se va a modificar al empleado " + empNo  );
            Empleados empleado = new Empleados();
           
            empleado = (Empleados) sesion.load(Empleados.class, (short) empNo); //carga el empleado usando el numero de identifiacdor pasado como argumento
            empleado.setSalario((float) sal); //modifica el salario
            empleado.setComision((float) com); //modifica la comision
            
            sesion.save(empleado);
            tx.commit();            
        } catch (HibernateException hex){
            System.err.println("Error " + hex.getCause() + ", " + hex.getMessage());
        } finally {
            sesion.close();
        }
        
    }
    /**
     * Borra un empleado por su número de empleado
     * @param empNo
     */
    public static void borraEmp(short empNo){
        Session sesion = null;
        try{
            //obtiene una sesión
            SessionFactory sf = HibernateUtil.getSessionFactory();
            //abre la sesión
            sesion = sf.openSession();
            //comienza una transacción
            Transaction tx = sesion.beginTransaction();
            System.out.println("se va a borrar al empleado " + empNo  );
            Empleados empleado = new Empleados();
           
            empleado = (Empleados) sesion.load(Empleados.class, (short) empNo); //carga el empleado usando el numero de identifiacdor pasado como argumento
            
            sesion.delete(empleado); //Borra de la base de datos 
            
            tx.commit();            
        } catch (HibernateException hex){
            System.err.println("Error " + hex.getCause() + ", " + hex.getMessage());
        } finally {
            sesion.close();
        }
        
    }
    
    public static void insertaEmpleado(short empNo, Departamentos depart, String apellido, String oficio, Short dir, Date fechaAlt, Float salario, Float comision){
        Session sesion = null;
        try{
            //obtiene una sesion
            SessionFactory sf = HibernateUtil.getSessionFactory();
            //abre la sesion
            sesion = sf.openSession();
            //comienza una transaccion
            Transaction tx = sesion.beginTransaction();
            System.out.println("Inserta una fila en la tabla de Empleados");
            //inserta el departamento dado en la tabla departamentos
            
            emp = new Empleados();
            emp.setEmpNo((short) empNo);
            emp.setDepartamentos(depart);
            emp.setApellido(apellido);
            emp.setComision(comision);
            emp.setDir(dir);
            emp.setFechaAlt(fechaAlt);
            emp.setOficio(oficio);
            emp.setSalario(salario);
            
            sesion.save(emp);
            tx.commit();
            
            
        } catch (HibernateException hex){
            System.err.println("Error " + hex.getCause() + ", " + hex.getMessage());
        } finally {
            sesion.close();
        }
    }
    
    
    /**
     * Método para listar los departamentos de la BD 
     */
    public static void listadoDepartamentos(){
        //obtener sesion y abrir
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        System.out.println("##############################");
        System.out.println("Listado de Departamentos");
        
        Departamentos dep = new Departamentos();
        //consulta con HQL
        Query q = session.createQuery("from Departamentos");
        //Iterador para recorrer el resultado de la consulta
        Iterator<?> iter = q.iterate();
        //se recorre mientrs haya resultados
        while (iter.hasNext()){
            dep = (Departamentos) iter.next();
            System.out.println(dep.getDeptNo() + "*" + dep.getDnombre());
        }
        System.out.println("###############################");
        
        session.close();
    }
    
    /**
     * Método para insertar un departamento con sus datos en la BD
     * @param num
     * @param nombre
     * @param localidad 
     */
    public static void insertaDepartamento(byte num, String nombre, String localidad){
        Session sesion = null;
        try{
            //obtiene una sesion
            SessionFactory sf = HibernateUtil.getSessionFactory();
            //abre la sesion
            sesion = sf.openSession();
            //comienza una transaccion
            Transaction tx = sesion.beginTransaction();
            System.out.println("Inserta una fila en la tabla de Departamentos");
            //inserta el departamento dado en la tabla departamentos
            Departamentos dep = new Departamentos();
            dep.setDeptNo((byte)num);
            dep.setDnombre(nombre);
            dep.setLoc(localidad);
            sesion.save(dep);
            tx.commit();
            
            
        } catch (HibernateException hex){
            System.err.println("Error " + hex.getCause() + ", " + hex.getMessage());
        } finally {
            sesion.close();
        }
    }
    
}
