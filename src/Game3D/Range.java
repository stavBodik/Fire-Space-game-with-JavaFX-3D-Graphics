package Game3D;

/**
 * This class represent float range of values .
 * @author Stanislav Bodik
 */

public class Range {

    private float min;
    private float max;

    public Range(float min, float max){
        this.min = min;
        this.max = max;
    }

	public float getMin() {
		return min;
	}

	public void setMin(float min) {
		this.min = min;
	}

	public float getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
    
    
}