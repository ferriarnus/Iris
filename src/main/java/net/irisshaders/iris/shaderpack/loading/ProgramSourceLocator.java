package net.irisshaders.iris.shaderpack.loading;

import net.irisshaders.iris.shaderpack.programs.ProgramSet;
import net.irisshaders.iris.shaderpack.programs.ProgramSource;

import java.util.Optional;

public interface ProgramSourceLocator {

    ProgramSourceLocator EMPTY = set -> Optional.empty();

    Optional<ProgramSource> findSource(ProgramSet set);
}
