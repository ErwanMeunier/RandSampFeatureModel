package randsampFM;

import de.neominik.uvl.ast.UVLModel;

public final class FmXor extends OthersFM {

	public FmXor() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public long count() {
		return children.stream().mapToLong(x->x.count()).sum() + 1;
	}
}
