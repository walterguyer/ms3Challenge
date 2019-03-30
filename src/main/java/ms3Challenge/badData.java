package ms3Challenge;

import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Getter;
import lombok.Setter;

@DatabaseTable(tableName = "bad_data")
@Getter
@Setter
public class badData {
	
	public static final String A_FIELD_NAME = "A";
	public static final String B_FIELD_NAME = "B";
	public static final String C_FIELD_NAME = "C";
	public static final String D_FIELD_NAME = "D";
	public static final String E_FIELD_NAME = "E";
	public static final String F_FIELD_NAME = "F";
	public static final String G_FIELD_NAME = "G";
	public static final String H_FIELD_NAME = "H";
	public static final String I_FIELD_NAME = "I";
	public static final String J_FIELD_NAME = "J";
	
	@DatabaseField(generatedId = true)
	private Long id;
	
	@DatabaseField(columnName = A_FIELD_NAME)
	private String A;
	
	@DatabaseField(columnName = B_FIELD_NAME)
	private String B;
	
	@DatabaseField(columnName = C_FIELD_NAME)
	private String C;
	
	@DatabaseField(columnName = D_FIELD_NAME)
	private String D;
	
	@DatabaseField(columnName = E_FIELD_NAME)
	private String E;
	
	@DatabaseField(columnName = F_FIELD_NAME)
	private String F;
	
	@DatabaseField(columnName = G_FIELD_NAME)
	private String G;
	
	@DatabaseField(columnName = H_FIELD_NAME)
	private String H;
	
	@DatabaseField(columnName = I_FIELD_NAME)
	private String I;
	
	@DatabaseField(columnName = J_FIELD_NAME)
	private String J;
	
	public badData() {}
	
	public badData(List<String> records) {
		this.A = records.get(0);
		this.B = records.get(1);
		this.C = records.get(2);
		this.D = records.get(3);
		this.E = records.get(4);
		this.F = records.get(5);
		this.G = records.get(6);
		this.H = records.get(7);
		this.I = records.get(8);
		this.J = records.get(9);
	}
	
}