package randsampFM;

import de.neominik.uvl.ast.UVLModel;

public final class FmXor extends OthersFM {

	public FmXor() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int count() {
		return children.stream().mapToInt(x->x.count()).sum() + 1;
	}
}
