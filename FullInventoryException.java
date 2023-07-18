//=============================================================================
// FullInventoryException
//-----------------------------------------------------------------------------
// Exception caught when player inventory is full (when picking up item with
//  more than 8 items already on inventory).
//=============================================================================

public class FullInventoryException extends Exception {
	public FullInventoryException() {

	}

	public String getMessage() {
		return "Your inventory is Full";
	}
}
