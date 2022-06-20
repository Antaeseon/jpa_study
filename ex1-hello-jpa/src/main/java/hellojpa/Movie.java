package hellojpa;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@DiscriminatorValue("M")
@PrimaryKeyJoinColumn(name = "MOVIE_ID")
public class Movie extends Item {

	private String director;
	private String actor;

	public void setDirector(String director) {
		this.director = director;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}
}
