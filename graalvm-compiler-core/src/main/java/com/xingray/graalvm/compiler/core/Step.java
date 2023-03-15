package com.xingray.graalvm.compiler.core;

/**
 * Define the different steps that can be handled by
 * the SubstrateDispatcher. The steps are only used when
 * the dispatcher is launched through the main method.
 */
public enum Step {
    /**
     * The goal of the COMPILE step is to run GraalVM
     * native-image to generate a compiled object file.
     */
    COMPILE(),
    /**
     * The goal of the LINK step is to link all the
     * libraries into an executable or a shared library,
     * depending on the target platform.
     */
    LINK(COMPILE),
    /**
     * The goal of the PACKAGE step is to generate an
     * application package that can be distributed.
     */
    PACKAGE(LINK),
    /**
     * The goal of the INSTALL step is to install the
     * generated application package on a supported
     * target (either the host machine or an attached
     * device).
     */
    INSTALL(PACKAGE),
    /**
     * The goal of the RUN step is to run the installed
     * application. It might also take a shortcut and
     * directly run the executable that was produced by
     * the LINK step.
     */
    RUN(INSTALL);

    private final Step dep;

    Step() {
        this.dep = null;
    }

    Step(Step dep) {
        this.dep = dep;
    }

    /**
     * Checks if the provided <code>step</code> is required to be
     * executed for <code>this</code> step.
     *
     * @param step the step to check
     * @return <code>true</code> if the provided step needs to run
     */
    public boolean requires(Step step) {
        return this == step || (dep != null && dep.requires(step));
    }
}