package randsampFM;

import de.neominik.uvl.ast.UVLModel;

public final class FmOR extends OthersFM {

	public FmOR(UVLModel uvlModel) {
		super(uvlModel);
		// TODO Auto-generated constructor stub
	}

	public long count() {
		return children.stream().mapToLong(x->x.count()+1).reduce(1, (a,b)-> a*b)-1;
	}
}
