package gbaloyi.swingy.model.artifact;

import java.io.Serializable;

import lombok.Getter;


@Getter
public abstract class Artifact implements Serializable {

    private static final long serialVersionUID = 2776303584447042497L;
    
    String name;
    ArtifactEnum type;

    Artifact(String name) {

        this.name = name;
    }
}