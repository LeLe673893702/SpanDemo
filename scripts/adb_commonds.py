import io
import os
import subprocess
import sys

# class AdbCommands(object):
#     def Compiler(self, flavor, debugOrRelease):
#         if debugOrRelease is 'R' :
#             build_type = 'Release'
#         else:
#             build_type = 'Debug'
#         cmd = ['./gradlew clean build assemble', flavor, build_type]
#         print("".join(cmd))
#         proc = subprocess.Popen("cd .. && " +"".join(cmd), shell=True)
#         proc.communicate()
#         return proc
#
#     def Install(self, apk_path, replace_existing=True,
#                 grant_permissions=False, timeout_ms=10000, transfer_progress_callback=None):
#         if not os.path.exists(apk_path):
#             raise FileNotFoundError("file not found")
#         cmd = ['adb install']
#         if grant_permissions:
#             cmd.append(' -g')
#         if replace_existing:
#             cmd.append(' -r')
#
#         cmd.append(' ' + apk_path)
#         print("".join(cmd))
#         proc = subprocess.Popen("".join(cmd), shell=True)
#         proc.communicate()
#         return proc
#
#
#     def Launch(self):
#         package_name = 'com.example.spandemo'
#
#         launch_cmd = ["adb shell am start-activity -n ",
#                       package_name, "/.MainActivity"]
#         subprocess.Popen("".join(launch_cmd), shell=True)
#
# def main(argv):
#     c = AdbCommands()
#     flavor = ""
#     build_type = ""
#     if len(argv) > 1:
#         flavor = argv[1]
#     if len(argv) > 2:
#         build_type = argv[2]
#     result1 = c.Compiler(flavor, build_type)
#     # if result1.returncode:
#     result2 = c.Install("../app/build/outputs/apk/debug/app-debug.apk")
#         # if result2.returncode:
#     c.Launch()
if __name__ == "__main__":
    # main(sys.argv)
    cmd = ['./gradlew assembleDebug']

    proc = subprocess.Popen("".join(cmd), shell=True)
    proc.communicate()