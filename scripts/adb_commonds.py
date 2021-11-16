import io
import os
import subprocess
import sys

class AdbCommands(object):
    def Compiler(self, flavor, debugOrRelease):
        if debugOrRelease is 'R' :
            build_type = 'Release'
        else:
            build_type = 'Debug'
        subprocess.Popen("chdir", shell=True)
        cmd = ['gradlew clean build assemble', flavor, build_type, " --info"]
        print("".join(cmd))
        proc = subprocess.Popen("cd .. && " +"".join(cmd), shell=True)
        proc.communicate()
        return proc

    def Install(self, apk_path, replace_existing=True,
                grant_permissions=False, timeout_ms=10000, transfer_progress_callback=None):
        if not os.path.exists(apk_path):
            raise FileNotFoundError("文件不存在")
        cmd = ['adb install']
        if grant_permissions:
            cmd.append(' -g')
        if replace_existing:
            cmd.append(' -r')

        cmd.append(' ' + apk_path)
        print("".join(cmd))
        proc = subprocess.Popen("".join(cmd))
        proc.communicate()
        return proc


    def Launch(self, apk_path):
        cmd = ['aapt dump']
        if not os.path.exists(apk_path):
            raise FileNotFoundError("文件不存在")

        get_package_name_cmd = ['aapt2 dump packagename ', apk_path]
        get_package_name_proc = subprocess.Popen("".join(get_package_name_cmd), stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
        package_name = get_package_name_proc.communicate()[0].decode('utf-8',"ignore")
        print(get_package_name_proc.communicate()[1].decode("gbk", "ignore"))

        launch_cmd = ["adb shell am start-activity -n ",
                      str(package_name).strip(), "/.MainActivity"]
        print("".join(launch_cmd))
        launch_proc = subprocess.Popen("".join(launch_cmd))
        print(launch_proc.communicate()[0])

def main(argv):
    c = AdbCommands()
    flavor = ""
    build_type = ""
    if len(argv) > 1:
        flavor = argv[1]
    if len(argv) > 2:
        build_type = argv[2]
    result1 = c.Compiler(flavor, build_type)
    if result1.returncode:
        result2 = c.Install("..//app//build//outputs//apk//debug//app-debug.apk")
        if result2.returncode:
            c.Launch("..//app//build//outputs//apk//debug//app-debug.apk")
if __name__ == "__main__":
    main(sys.argv)