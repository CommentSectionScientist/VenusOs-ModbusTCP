package de.ossi.wolfsbau;

import java.io.BufferedReader;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import de.ossi.wolfsbau.anfrager.WRAnfrager;
import de.ossi.wolfsbau.db.DBModel;
import de.ossi.wolfsbau.db.util.XMLtoDBConverter;
import de.ossi.wolfsbau.modbus.ForbiddenAccessException;
import de.ossi.wolfsbau.modbus.ModbusTCPReader;
import de.ossi.wolfsbau.modbus.ModbusTCPWriter;
import de.ossi.wolfsbau.modbus.data.ModbusDevice;
import de.ossi.wolfsbau.modbus.data.ModbusOperation;
import de.ossi.wolfsbau.modbus.data.ModbusResultInt;
import de.ossi.wolfsbau.parser.WRAntwortParser;
import de.ossi.wolfsbau.xml.XDevice;
import de.ossi.wolfsbau.xml.XRoot;

public class Starter {

	private static final String JDBC_PATH = "jdbc:sqlite:wr.sqlite";
	private static final String IP_WECHSELRICHTER_WEST = "192.168.0.82";
	private static final String IP_WECHSELRICHTER_OST = "192.168.0.83";
	private static final String IP_VICTRON = "192.168.0.81";
	private static final int MODBUS_DEFAULT_PORT = 502;

	private final WRAnfrager anfrager = new WRAnfrager(IP_WECHSELRICHTER_WEST);
	private final WRAntwortParser parser = new WRAntwortParser();
	private final DBModel schreiber = new DBModel(JDBC_PATH);
	private final ModbusTCPReader modbusReader = new ModbusTCPReader(IP_VICTRON, MODBUS_DEFAULT_PORT);
	private final ModbusTCPWriter modbusWriter = new ModbusTCPWriter(IP_VICTRON, MODBUS_DEFAULT_PORT);

	public static void main(String[] args) throws IOException, JAXBException, ForbiddenAccessException {
		Starter starter = new Starter();
//		starter.speichereAktuelleWRDaten();
//		starter.speichereAktuelleVictronDaten();
		starter.schreibeVictronDaten();
	}

	public void speichereAktuelleWRDaten() throws IOException, JAXBException {
		// REST Anfrage
		BufferedReader reader = anfrager.frageDatenAb();

		// REST Antwort (XML) Parsen
		XRoot root = parser.leseEin(reader);
		XDevice dev = root.getDevice();

		// XML Objekt in Entität umwandeln und un DB Speichern
		schreiber.saveDevice(XMLtoDBConverter.from(dev));
	}
	
	public void schreibeVictronDaten() throws ForbiddenAccessException {
		//Before
		System.out.println("Before:");
		ModbusResultInt before = modbusReader.readOperationFromDevice(ModbusOperation.HUB_ESS_CONSTROL_LOOP_SETPOINT, ModbusDevice.VE_CAN_AND_SYSTEM_DEVICE_0);
		System.out.println(before.toString());
		//Write
		modbusWriter.writeOperationFromDevice(ModbusOperation.HUB_ESS_CONSTROL_LOOP_SETPOINT, ModbusDevice.VE_CAN_AND_SYSTEM_DEVICE_0, 0);
		//After
		System.out.println("After:");
		ModbusResultInt after = modbusReader.readOperationFromDevice(ModbusOperation.HUB_ESS_CONSTROL_LOOP_SETPOINT, ModbusDevice.VE_CAN_AND_SYSTEM_DEVICE_0);
		System.out.println(after.toString());
	}

	public void leseVictronDaten() {
		ModbusResultInt stateOfCharge = modbusReader.readOperationFromDevice(ModbusOperation.SYS_BATTERY_SOC_SYSTEM, ModbusDevice.VE_CAN_AND_SYSTEM_DEVICE_0);
		System.out.println(stateOfCharge.toString());
	}

}
