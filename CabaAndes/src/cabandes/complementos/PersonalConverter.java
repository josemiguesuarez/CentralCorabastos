package cabandes.complementos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class PersonalConverter {
	
	public final static String CLASE_DATO_PORCENTAJE_0_1 = "de cero a uno";
	public static String TypetoString ( char tipoChar )
	{
		if(tipoChar == 'P')
		{
			return "Perishable";
		}
		else if(tipoChar == 'R')
		{
			return "Refrigerated";
		}
		else if (tipoChar == 'N')
		{
			return "Non perishable";
		}
		else
		{
			return "No valid Char";
		}
		
	}
	
	public static Double limpiarParaUtilizarComoDouble( String numero, String claseDeDato) throws Exception
	{
		if ( numero.contains("."))
		{
			numero = numero.replaceAll(",", "");
			System.out.println("Numero " + numero);
		}
		if ( numero.replaceFirst(".", "").contains("."))
		{
			System.out.println("Numero " + numero);
			throw new  Exception ("The Number can not be conversed because it has more than two dots 1");
			
		}
		if ( numero.contains(","))
		{
			numero = numero.replace(",", ".");
			System.out.println("Numero " + numero);
			if ( numero.contains(","))
			{
				throw new  Exception ("The Number can not be conversed because it has more than two dots 2");
			}
		}
		double resp = Double.parseDouble(numero);
		
		if ( claseDeDato != null)
		{
			if (claseDeDato.equals(CLASE_DATO_PORCENTAJE_0_1))
			{
				if ( resp <0 || resp> 100)
				{
					throw new  Exception ("Number Should be between 0 and 100");
				}
				else if ( resp >= 1)
				{
					return resp/100;
				}
				else
				{
					return resp;
				}
			}
		}
		
		
		return resp;
	}
	
	public static String getDateFormatForDB ( Date fecha)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		return "TO_DATE('"+dateFormat.format(fecha)+"', 'yyyy/mm/dd')";
	}
	public static String getDateFormatForDB ( String fecha)
	{
		return "TO_DATE('"+fecha+"', 'yyyy/mm/dd')";
	}

}
