# nevex_dots_and_boxes_android

An Android game based on the Dots and Boxes game - also includes my custom NeVeX A.I

### Release Version History

#####v0.2.3
* fixed canvas null bug on surfaceholder locking
* changed minimum ai delay controls to custom buttons 
* fixed bug with game end for human player winning - board did not color to winner
* current selected color now not modified on player board coloring

#####v0.2.2
* fixed bug where ai engines not stopping on game looper thread pause
* added threading pause on AI turn (not after same AI or other AI)
* text for ai moves say "is computing" on their turn. "AI to move" will only briefly appear
* fixed bug with motion events: getX/Y returns relative values instead of absolute values. All values now used absolute raw values
* improved animation consitent speeds (still not the best)
* added pause for ai engines on concurrent moves
* added board colors for complete board on ai move
* board colors to the winner on board completion
* added in new ai engine delay setting (which can be modified between bounds)
* modified threading of AI engines to include waiting on moves before returning to next player

#####v0.2.1
* fixed > 2 players bug in main menu
* finished main menu modifications
* redrew the nevex logo on splash
* removed app versioning from various places -> only on main menu now
* general small bug fixes and layout cleanups

#####v0.2.0
* making the main menu prettier - resizing everything and creating more parent layouts -> very fucking annoying and still not completed
* scrollviews to only the board options
* game start and settings to the bottom 
* intro screens modified slightly

#####v0.1.9
* change behaviour at end of game -> thread still loop runs again to update views but gamemanager is not called
* addded new marx splash screen
* added animations to splash screen entrance

#####V0.1.8
* fixed scale bug -> could not translate canvas when zooming to certain scale factors
* fixed issue where game tries to resume when game is over -> select settings then return; the thread resumes and moves to next player
* swtiched engines -> Nevex is Viper....Viper is Nevex....Viper is better, so rebranded as NeVeX.

#####v0.1.7
* implemented saved state idea for when threads are restarted on the level -> ui/managers/ai etc.. are restored
* disabled game buttons on game end
* reconfigured game buttons on game start
* redesigned the way the app shutsdown on an engine failure -> finish method on activity
* undo button enchanced - only clickable when moves to perform
* rework of undo button from previous version - updates are driven from gamemanager only now
* rework of splash screen - no picture, text is os drawn - timer for helper text added
* last move is no longer the "current" color...defaults to player color
* ai engine does not go into infinite loop making moves - 5 tries - extension on 0.1.6 change
* added resetting of ai engines - fixes issues on initial start after undo game.
* bug fix: game over, then select settings....game tries to resume on thread recreation

#####v0.1.6
* added colors.xml
* added Color class - all colors now route through this
* added gameover thread sleep to fix issue where game "drawing" did not complete when game over
* change color scheme
* added retries to AI engine moves - 5 times. If fails after 5...app sends message to UI and force shutdown occurs - though not gracefully
* small bug fixes

#####v0.1.5
* NeVex engine is noW viper (name switch in code)
* menu cleanup - player forced selections with checboxes - to have more then 2 players, you must select the options
* settings menu cleanup
* added enabling viewgroup code (linearlayouts..etc)
* extracted dynamic colors into player class and application class - need to centralize this more
* added colors from hex

#####v0.1.4
* fixed bug where game borders where not being restricted, border was getting surpassed.
* seperated settings in boardSettings and applicationsettings classes
* create thread control GAME_RUNNING boolean. thread shut down at gameover 
* move touched pointers to debug mode
* added settings control from main menu
* remodeled main menu. extracted text sizes. added more relative layouts. better layouts defined.

#####v0.1.3
* added splash screen activity and layout
* added settings screen activity and layout
* move seizure and animate buttons to settings menu and out from level manager
* added debug mode switch
* fixed thread issues with settings menu activations and resuming
* fixed animation coloring bug after seizure mode (colors were not defaulted back)
* added game pause and resuming - thread management. on focus lost and surface destroyed, thread is killed. On resume of focus or surface created, new thread with previous game state started.
* added version numbers to splash screen, main menu and settings menu
* added app icon 

#####v0.1.2
* debug mode added - timed moves and time game board completion
* initial unit testing of gameactivity
* added UiupdateManager class for ui thread update handling. some tight coupling removed from gamemanager

#####v0.1.1
* added in zoom buttons with link to scaledectetor. buttons show red when range surpassed.
* new game score winner method written to determine winner
* game wont start without two valid players now. need to enhance this with messages to user

### Development Builds Release History

#####dev-build-44
* rewrote boardsettings class
* boardsettings scale to fit screen on initial startup - constructor based - need to add in height adjustment for ui parts
 * the buttons on bottom and the text on the top
* fixed some issues with scaling 
* added MyLog class - cleaner logging across app 
* implemented two touch shapes -> android event coOrds [Green Circle] and my event translated/scaled coOrds [White circle] for visual touch testing events
* border scaling issue fixed (border bounds enforced on scaling and translations)
* rewrote the Y coOrd offsetting that the board height (the canvas too)
* refactored packages and classes into better structures
* cleaned up xml files with better id/string names and contents
* randomized player start

#####dev-build-35
* modified touch time and using touch "modes" to determine what actions are being performed
* added "error" zones from edges attempted to be selected - user can touch "near" the edge and it will be selected
* added history to each shape - reversal after seizure mode to original state - colors etc
* fixed bug in undo move where player was not forced to make a move when they should be (needs to be able to judge to skip over previous player moves too)
* improved undo move functionality - undo move will keep undoing until player whom initiated undo, becomes current player again, or the board is empty.
* locked screen orientation to portrait

#####dev-build-29
* initial scaling added - a bit wonky though
* translations of touch events include scalefactors

#####dev-build-22
* removed async'd threads for ai engines - too slow and it is better now as full multithread with the ui handler control
* implemented threads for view
* subclass my view 
* view is now surface view
* removed invalidate - ondraw -> methodoloy. Now it is a while game loop with fps counters
* made ai engines multithreaded using thread class with handlers back to UI
* bug fixes
* enhanced seizure mode

#####dev-build-13
* bug fixes
* enhanced seizure mode

#####dev-build-9:
* async'd ai engines
* intial animations of box tokens ( 2 implementations - one async, one onDraw direct modify)
* undo functionality
* mycircleShape

#####dev-build-5 and before is the initial big bang
