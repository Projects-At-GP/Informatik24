import Redfoot.Game;

/**
 * <p>
 * This is just a gate to make Greenfoot happy.
 * The Greenfoot-App doesn't allow us to enter `Redfoot.Game` directly as the last instantiated world,
 * so this is the simple workaround.
 * </p>
 * <p>
 * We organized everything in packages to take advantage of having files in folders and accessing package-contents
 * inside other packages, as Java doesn't allow to access files from the working directory if they aren't packaged.
 * </p>
 * <p>
 * In case you created a subclass of `Greenfoot.World` to have a new world, you screwed up, as the Greenfoot-App
 * doesn't allow you to set a world as last instantiated if it doesn't recognize it as a subclass of `Greenfoot.World`.
 * </p>
 * <p>
 * But we're here to help:
 * <ol>
 *     <li>Close the Greenfoot-App.</li>
 *     <li></li>
 * </ol>
 */
public class Gate extends Game {}
