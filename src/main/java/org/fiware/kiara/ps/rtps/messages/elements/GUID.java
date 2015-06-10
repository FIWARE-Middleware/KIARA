package org.fiware.kiara.ps.rtps.messages.elements;


public class GUID {
	
	private GUIDPrefix m_guidPrefix;
	
	private EntityId m_entityId;
	
	public GUID() {
		this.m_guidPrefix = new GUIDPrefix();
		this.m_entityId = new EntityId();
	}
	
	public GUID(GUIDPrefix guidPrefix, EntityId entityId) {
            this.m_guidPrefix = guidPrefix;
            this.m_entityId = entityId;
    }
	
	public GUIDPrefix getGUIDPrefix() {
		return this.m_guidPrefix;
	}
	
	public void setGUIDPrefix(GUIDPrefix guidPrefix) {
		this.m_guidPrefix = guidPrefix;
	}
	
	public EntityId getEntityId() {
		return this.m_entityId;
	}
	
	public void setEntityId(EntityId id) {
		this.m_entityId = id;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof GUID) {
			return this.m_guidPrefix.equals(((GUID) other).m_guidPrefix) && this.m_entityId.equals(((GUID) other).m_entityId);
		}
		return false;
	}

}
