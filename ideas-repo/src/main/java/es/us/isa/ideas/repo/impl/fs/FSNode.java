package es.us.isa.ideas.repo.impl.fs;

import es.us.isa.ideas.repo.Node;



public class FSNode extends Node {

	private String icon;
	private String keyPath;
        private String description;

	public String getKeyPath() {
		return keyPath;
	}

	public void setKeyPath(String keyPath) {
		this.keyPath = keyPath;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}       

        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
        }                
        

	@Override
	public String toString() {
		String s = "{";
		
		s+= "\"title\":\"" + getTitle() + "\",";
                if(getDescription()!=null)
                    s+= "\"tooltip\":\""+ getDescription().replace("\n", "\\n")+ "\",";
		s+= "\"icon\":" + getIcon() + ",";
		s+= "\"isFolder\":" + isFolder() + ",";
		s+= "\"keyPath\":\"" + getKeyPath() + "\",";
		s+= "\"children\": [";
		
			for ( int i = 0; i < getChildren().size() ; i++ ) {
				if (  i != 0 ) {
					s += ",";
				}
				FSNode child =  (FSNode) getChildren().get(i);
				s+= child.toString();
			}
		
		s+= "]";
		s += "}";
		return s;
	}
	
}
