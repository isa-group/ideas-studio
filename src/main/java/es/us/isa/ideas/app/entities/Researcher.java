package es.us.isa.ideas.app.entities;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import es.us.isa.ideas.app.security.UserAccount;

/**
 *
 * @author japarejo
 */

@Entity
@Access(AccessType.PROPERTY)
public class Researcher extends Actor implements Cloneable {
	private static final long serialVersionUID = -1518460708731921159L;

	protected Collection<Experiment> experiments;

	protected Collection<SocialNetworkConfiguration> socialNetworkConfigurations;

	public Researcher() {
		super();
		experiments = new HashSet<Experiment>();
		socialNetworkConfigurations = new HashSet<SocialNetworkConfiguration>();
	}

	@NotNull
	@OneToMany(mappedBy = "actor", cascade = { CascadeType.ALL })
	public Collection<SocialNetworkConfiguration> getSocialNetworkConfigurations() {
		return socialNetworkConfigurations;
	}

	public void setSocialNetworkConfigurations(
			Collection<SocialNetworkConfiguration> socialNetworkConfiguarions) {
		this.socialNetworkConfigurations = socialNetworkConfiguarions;
	}

	public SocialNetworkConfiguration getSocialNetworkConfiguration(
			String service) {
		SocialNetworkConfiguration config = null;
		for (SocialNetworkConfiguration candidate : socialNetworkConfigurations) {
			if (service.equalsIgnoreCase(candidate.getService())) {
				config = candidate;
				break;
			}
		}
		return config;
	}

	@NotNull
	@OneToMany(mappedBy = "owner", cascade = { CascadeType.ALL })
	public Collection<Experiment> getExperiments() {
		return experiments;
	}

	public void setExperiments(Collection<Experiment> experiments) {
		this.experiments = experiments;
	}

	public void addExperiment(Experiment exp) {
		exp.setOwner(this);
		if (!experiments.contains(exp))
			experiments.add(exp);
	}

	public void removeExperiment(Experiment exp) {
		experiments.remove(exp);
	}

	@Override
	public Researcher clone() throws CloneNotSupportedException {
		Researcher res = (Researcher) super.clone();
		UserAccount ua = res.getUserAccount().clone();
		res.setId(0);
		res.setUserAccount(ua);
		return res;
	}

}
