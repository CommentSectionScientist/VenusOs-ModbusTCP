package de.ossi.modbustcp.gui;

import java.time.format.DateTimeFormatter;

import ca.odell.glazedlists.gui.TableFormat;

/**
 * Tableformat welches angibt, wie die Tabelle aussieht, also Spalten Anzahl und Bezeichnung.
 * @author ossi
 *
 */
class DeviceOperationResultTableFormat implements TableFormat<DeviceOperationResultTO> {
	
	private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Operation";
		case 1:
			return "Device";
		case 2:
			return "Time";
		case 3:
			return "Result";
		default:
			return "";
		}
	}

	@Override
	public Object getColumnValue(DeviceOperationResultTO baseObject, int column) {
		switch (column) {
		case 0:
			return baseObject.getOperation().getName();
		case 1:
			return baseObject.getModbusDevice().getName();
		case 2:
			return baseObject.getZeit() != null ? ISO_DATE.format(baseObject.getZeit()) : null;
		case 3:
			return baseObject.getErgebnis();
		default:
			return null;
		}
	}

}