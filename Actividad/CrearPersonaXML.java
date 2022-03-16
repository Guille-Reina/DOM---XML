package Actividad;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class CrearPersonaXML {

	 public static void main(String args[]) throws IOException{
		   File fichero = new File("Persona.dat");   //No exite un .dat de persona, por lo que no se puede crear su xml
		   RandomAccessFile file = new RandomAccessFile(fichero, "p");
		   
		   int  ruta, edad, posicion=0;        
		   char nombre[] = new char[10], aux; //Sustituyo el apellido por el nombre
		     
		   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		  
		   try{
		     DocumentBuilder builder = factory.newDocumentBuilder();
		     DOMImplementation implementation = builder.getDOMImplementation();
		     Document document = 
		     implementation.createDocument(null, "Persona", null);
		     document.setXmlVersion("1.0"); 
		   
		     for(;;) {
			 file.seek(posicion);
			 ruta=file.readInt(); //No tiene una id, pero es necesario saber su posición con resecto a las otras cadenas de elementos, por lo que se crea "ruta" 
		       for (int i = 0; i < nombre.length; i++) {
		         aux = file.readChar();
		         nombre[i] = aux;    
		       }   
		       String nombres = new String(nombre);
		       edad = file.readInt(); //Sustituyo departamento por edad
			   
			 if(ruta>0) {
			   Element raiz = document.createElement("persona");
		       document.getDocumentElement().appendChild(raiz); 
		        
		         CrearElemento("ruta",Integer.toString(ruta), raiz, document); 
		         
		         CrearElemento("nombre",nombres.trim(), raiz, document); 

		         CrearElemento("edad",Integer.toString(edad), raiz, document); 

			 }	
			 
			 posicion= posicion + 27; // Como no exite ningún elemento más y las personas solo puede tener más 3 digitos en la edad, ponemos 27 en vez de 36
			 if (file.getFilePointer() == file.length()) break; 

		     }
				
		     Source source = new DOMSource(document);
		     Result result = new StreamResult(new java.io.File("Persona.xml"));        
		     Transformer transformer = TransformerFactory.newInstance().newTransformer();
		     transformer.transform(source, result);
		    
		    }catch(Exception e){ System.err.println("Error: "+e); }
		    
		    file.close();
		 }
		 
	 static void  CrearElemento(String datoPers, String valor, Element raiz, Document document){
		    Element elem = document.createElement(datoPers); 
		    Text text = document.createTextNode(valor);
		    raiz.appendChild(elem);
		    elem.appendChild(text);	 	
		 }
		}
